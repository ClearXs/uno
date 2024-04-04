package cc.allio.uno.data.orm.dsl.helper;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.api.Step;
import cc.allio.uno.core.bean.MapWrapper;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.type.TypeOperatorFactory;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.*;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.annotation.LogicDelete;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JdbcType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 定义Pojo与SQL相关的操作
 * <p>注：pojo的字段驼峰</p>
 * <p>包含如下操作集（获取基于{@link Object}pojo对象或者{@link Class}pojoClass）：</p>
 * <ul>
 *     <li>获取{@link Table}</li>
 *     <li>获取{@link ColumnDef}</li>
 *     <li>获取主键的{@link ColumnDef}</li>
 *     <li>基于{@link ColumnDef}获取对应的值</li>
 *     <li>增强所有get value方法，根据{@link ColumnDef#getDataType()}转换对应值类型</li>
 * </ul>
 * <p>注意：</p>
 * <ul>
 *     <li>该类所有getXX方法的字段名称全是驼峰命名，除了{@link #getValueByColumn(String)}、{@link #getValueByColumn(ColumnDef)}</li>
 *     <li>所有setXX方法的字段名称全是驼峰命名</li>
 * </ul>
 *
 * @author j.x
 * @date 2023/7/4 17:14
 * @see #getInstance(Object)
 * @since 1.1.4
 */
public class PojoWrapper<T> implements ValueWrapper {

    private static final String ID = "id";
    @Getter
    private T pojo;
    private Class<?> pojoClass;
    private List<Field> pojoFields;
    @Getter
    private Table table;
    // 字段集合
    @Getter
    private List<ColumnDef> columnDefs;
    // 主键字段
    @Getter
    private ColumnDef pkColumn;
    // 删除标志字段
    @Getter
    private ColumnDef deletedColumn;
    // 非主键字段
    @Getter
    private List<ColumnDef> notPkColumns;
    private Map<DSLName, ColumnDef> columnDefMap;

    // 内置ValueWrapper做装饰作用
    private final ValueWrapper valueWrapper;

    static final ExpressionTemplate DOLLAR_TEMPLATE = ExpressionTemplate.createTemplate(Tokenizer.DOLLAR_BRACE);
    static final PojoInspect DEFAULT_POJO_INSPECT = new DefaultPojoInspect();
    static final Map<Class<?>, PojoInspect> POJO_INSPECTS = Maps.newConcurrentMap();
    static final Map<Class<?>, TableResolver> TABLE_RESOLVES = Maps.newConcurrentMap();
    static final Map<Class<?>, ColumnDefListResolver> CLASS_COLUMN_LIST_RESOLVERS = Maps.newConcurrentMap();
    static final Map<Field, ColumnDefResolver> COLUMN_RESOLVES = Maps.newConcurrentMap();
    // 存储以Class为key，PojoWrapper为实例的Map缓存
    static final Map<Class<?>, PojoWrapper<?>> POJO_INSTANCES = Maps.newConcurrentMap();

    PojoWrapper(T pojo) {
        if (Types.isMap(pojo.getClass())) {
            this.valueWrapper = new MapWrapper((Map<String, Object>) pojo);
        } else {
            this.valueWrapper = new ObjectWrapper(pojo);
        }
        this.pojo = pojo;
        init(pojo.getClass());
    }

    PojoWrapper(Class<?> pojoClass) {
        if (Types.isMap(pojoClass)) {
            this.valueWrapper = new MapWrapper();
        } else {
            this.valueWrapper = new ObjectWrapper(pojoClass);
        }
        init(pojoClass);
    }

    void init(Class<?> pojoClass) {
        this.pojoClass = pojoClass;
        this.pojoFields = FieldUtils.getAllFieldsList(pojoClass);
        this.columnDefs = obtainColumnDefs();
        this.table = obtainTable();
        this.pkColumn = columnDefs.stream().filter(ColumnDef::isPk).findFirst().orElse(null);
        this.deletedColumn = columnDefs.stream().filter(ColumnDef::isDeleted).findFirst().orElse(null);
        this.notPkColumns = columnDefs.stream().filter(columnDef -> !columnDef.isPk()).toList();
        this.columnDefMap = columnDefs.stream().collect(Collectors.toMap(ColumnDef::getDslName, f -> f));
    }

    /**
     * 获取Primary Key字段值
     */
    public Object getPKValue() {
        ColumnDef id = getPkColumn();
        return getForce(id.getDslName().format(DSLName.HUMP_FEATURE));
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param column the column（需要驼峰）
     * @return the value
     */
    public Object getValueByColumn(String column) {
        return getForce(DSLName.of(column, DSLName.HUMP_FEATURE).format());
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param columnDef columnDef
     * @return the value
     */
    public Object getValueByColumn(ColumnDef columnDef) {
        return getForce(DSLName.of(columnDef.getDslName(), DSLName.HUMP_FEATURE).format());
    }

    /**
     * 获取该Pojo对象的表名
     * <p>按照以下三种步骤进行解析</p>
     * <ol>
     *     <li>优先从{@link PojoResolver#obtainTableResolver()}中取值,要求{@link #pojo}不为null</li>
     *     <li>再次从{@link TableResolve}中取值</li>
     *     <li>最后尝试按照jpa {@link jakarta.persistence.Table}注解中取值</li>
     * </ol>
     *
     * @return Table instance
     * @see TableResolve
     * @see PojoResolver
     */
    private Table obtainTable() {
        return Step.<Table>start()
                .then(() -> {
                    if (pojo instanceof PojoResolver pojoResolver) {
                        return Optional.ofNullable(pojoResolver.obtainTableResolver())
                                .map(c -> c.resolve(pojoClass))
                                .orElse(null);
                    }
                    return null;
                })
                .then(() -> {
                    TableResolve tableResolve = AnnotationUtils.findAnnotation(pojoClass, TableResolve.class);
                    return Optional.ofNullable(tableResolve)
                            .flatMap(e -> {
                                TableResolver tableResolver =
                                        TABLE_RESOLVES.computeIfAbsent(
                                                pojoClass,
                                                k -> {
                                                    Class<? extends TableResolver> tableResolverClass = tableResolve.value();
                                                    return Optional.ofNullable(tableResolverClass)
                                                            .map(ClassUtils::newInstance)
                                                            .orElse(null);
                                                });
                                return Optional.ofNullable(tableResolver);
                            })
                            .map(r -> r.resolve(pojoClass))
                            .orElse(null);
                })
                .then(() -> {
                    // 取jpa注解
                    jakarta.persistence.Table tableAnno = AnnotationUtils.findAnnotation(pojoClass, jakarta.persistence.Table.class);
                    String indexName = StringPool.EMPTY;
                    Table table = new Table();
                    if (tableAnno != null) {
                        indexName = tableAnno.name();
                        table.setCatalog(tableAnno.catalog());
                        table.setSchema(tableAnno.schema());
                    }
                    if (StringUtils.isEmpty(indexName)) {
                        // 取类名并转换为下划线命名
                        indexName = DSLName.of(pojoClass.getSimpleName(), DSLName.UNDERLINE_FEATURE).format();
                    }
                    if (pojo != null) {
                        indexName = DOLLAR_TEMPLATE.parseTemplate(indexName, findMapValuesForce());
                    }
                    table.setName(DSLName.of(indexName));
                    return table;
                })
                .end();
    }

    /**
     * 获取该pojo字段的解析的sql字段
     * <p>按照以下三种步骤进行解析</p>
     * <ol>
     *     <li>优先从{@link PojoResolver#obtainColumnDefListResolver()} 中取值，要求{@link #pojo}不为null</li>
     *     <li>再次从{@link ColumnDefListResolve}中取值</li>
     *     <li>或者按照{@link #pojoFields}调用{@link #createColumnDef(Field)}获取</li>
     * </ol>
     */
    private List<ColumnDef> obtainColumnDefs() {
        // TODO verify column duplicate
        return Optional.ofNullable(columnDefs)
                .orElseGet(() ->
                        Step.<List<ColumnDef>>start()
                                .then(() -> {
                                    if (pojo instanceof PojoResolver pojoResolver) {
                                        return Optional.ofNullable(pojoResolver.obtainColumnDefListResolver())
                                                .map(c -> c.resolve(pojoClass))
                                                .orElse(null);
                                    }
                                    return null;
                                })
                                .then(() -> {
                                    ColumnDefListResolve columnDefListResolve = AnnotationUtils.findAnnotation(pojoClass, ColumnDefListResolve.class);
                                    return Optional.ofNullable(columnDefListResolve)
                                            .flatMap(c -> {
                                                ColumnDefListResolver columnDefListResolver =
                                                        CLASS_COLUMN_LIST_RESOLVERS.computeIfAbsent(
                                                                pojoClass,
                                                                k -> {
                                                                    Class<? extends ColumnDefListResolver> columnDefResolverClass = columnDefListResolve.value();
                                                                    return Optional.ofNullable(columnDefResolverClass)
                                                                            .map(ClassUtils::newInstance)
                                                                            .orElse(null);
                                                                });
                                                return Optional.ofNullable(columnDefListResolver);
                                            })
                                            .map(r -> r.resolve(pojoClass))
                                            .orElse(Collections.emptyList());
                                })
                                .then(() ->
                                        pojoFields.stream()
                                                .map(this::createColumnDef)
                                                .toList())
                                .end());
    }

    /**
     * 根据java{@link Field}实例创建{@link ColumnDef}实例
     * <p>按照以下三种步骤进行解析</p>
     * <ol>
     *     <li>优先从{@link PojoResolver#obtainColumnDefResolver()} 中取值，要求{@link #pojo}不为null</li>
     *     <li>再次从{@link ColumnDefResolve}中取值</li>
     *     <li>
     * <p>检测指定字段的值是否包含以下jpa注解</p>
     * <ul>
     *     <li>{@link Id}</li>
     *     <li>{@link Column}</li>
     * </ul>
     * </li>
     * <li>
     *     <p>包含自定义注解</p>
     *     <ul>
     *         <li>{@link LogicDelete}</li>
     *     </ul>
     * </li>
     * </ol>
     *
     * @param field the field
     */
    private ColumnDef createColumnDef(Field field) {
        return Step.<ColumnDef>start()
                .then(() -> {
                    if (pojo instanceof PojoResolver pojoResolver) {
                        return Optional.ofNullable(pojoResolver.obtainColumnDefResolver())
                                .map(c -> c.resolve(field))
                                .orElse(null);
                    }
                    return null;
                })
                .then(() -> {
                    ColumnDefResolve columnDefResolve = AnnotationUtils.findAnnotation(field, ColumnDefResolve.class);
                    // ColumnDefResolve注解上解析器优先级最高
                    return Optional.ofNullable(columnDefResolve)
                            .flatMap(c -> {
                                ColumnDefResolver columnDefResolver = COLUMN_RESOLVES.computeIfAbsent(
                                        field,
                                        k -> {
                                            Class<? extends ColumnDefResolver> columnResolverClass = columnDefResolve.value();
                                            return Optional.ofNullable(columnResolverClass)
                                                    .map(ClassUtils::newInstance)
                                                    .orElse(null);
                                        });
                                return Optional.ofNullable(columnDefResolver);
                            })
                            .map(c -> c.resolve(field))
                            .orElse(null);
                })
                .then(() -> {
                    ColumnDef.ColumnDefBuilder builder = ColumnDef.builder();
                    // 解析id
                    Id id = field.getAnnotation(Id.class);
                    if (id != null) {
                        builder.isPk(true);
                    }
                    // 如果当前没有被@Id注解表示，尝试判断当前字段的名称是否为id
                    if (id == null && (field.getName().equals(ID))) {
                        builder.isPk(true);
                    }
                    LogicDelete logicDelete = field.getAnnotation(LogicDelete.class);
                    if (logicDelete != null) {
                        builder.isDeleted(true);
                        builder.undeleted(logicDelete.undeleted());
                        builder.deleted(logicDelete.deleted());
                    }
                    // 默认按照下划线进行处理
                    String maybeColumnName = field.getName();
                    builder.dslName(DSLName.of(maybeColumnName, DSLName.UNDERLINE_FEATURE));
                    // 解析是否包含@Column or @TableField
                    Column column = field.getAnnotation(Column.class);
                    if (column != null) {
                        builder.isNonNull(!column.nullable());
                        builder.isNull(column.nullable());
                        builder.isUnique(column.unique());
                        String columnName = column.name();
                        if (StringUtils.isNotBlank(columnName)) {
                            builder.dslName(DSLName.of(columnName, DSLName.UNDERLINE_FEATURE));
                        }
                    }
                    // 解析该字段的类型
                    Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().guessJdbcType(field.getType());
                    if (CollectionUtils.isNotEmpty(jdbcTypes)) {
                        // 只匹配第一个
                        JdbcType jdbcType = Lists.newArrayList(jdbcTypes).get(0);
                        DSLType guessSQLType = DSLType.getByJdbcCode(jdbcType.getJdbcCode());
                        TypeTranslator translator = TypeTranslatorHolder.getTypeTranslator();
                        DSLType sqlType = translator.translate(guessSQLType);
                        DataType type = DataType.create(sqlType);
                        builder.dataType(type);
                    }
                    return builder.build();
                })
                .end();
    }

    /**
     * 基于{@link #getColumnDefs()}转换为{@link DSLName}
     *
     * @return dsl name collection
     */
    public Collection<DSLName> getColumnDSLName() {
        if (ObjectUtils.isNotEmpty(columnDefs)) {
            return columnDefs.stream().map(ColumnDef::getDslName).toList();
        }
        return Collections.emptyList();
    }

    /**
     * 获取columns values 集合.
     *
     * @return Object
     */
    public List<Object> getColumnValues() {
        return columnDefs.stream()
                .map(column -> getForce(column.getDslName().format(DSLName.HUMP_FEATURE)))
                .toList();
    }

    @Override
    public Boolean contains(String name) {
        return valueWrapper.contains(name);
    }

    @Override
    public PropertyDescriptor find(String name, Class<?> clazz) {
        return valueWrapper.find(name, clazz);
    }

    @Override
    public <K> Mono<K> get(String name, Class<K> fieldType) {
        return valueWrapper.get(name, fieldType)
                .flatMap(value -> {
                    if (value == EMPTY_VALUE) {
                        return Mono.justOrEmpty(Optional.empty());
                    }
                    DSLName dslName = DSLName.of(name, DSLName.UNDERLINE_FEATURE);
                    Optional<DSLName> filterDslName = dslName.filter(columnDefMap.keySet());
                    return Mono.justOrEmpty(filterDslName)
                            .flatMap(columnDslName -> {
                                ColumnDef columnDef = columnDefMap.get(columnDslName);
                                return Mono.justOrEmpty(Optional.ofNullable(columnDef))
                                        .map(ColumnDef::getDataType)
                                        .map(DataType::getDslType)
                                        .mapNotNull(dslType -> TypeRegistry.getInstance().findJavaType(dslType.getJdbcType()))
                                        .map(javaType -> TypeOperatorFactory.translator(javaType.getJavaType()).convert(value));
                            })
                            .cast(fieldType)
                            .onErrorResume(ex -> Mono.just(value))
                            .defaultIfEmpty(value);
                });
    }

    @Override
    public Mono<Object> set(String name, Object... value) {
        return valueWrapper.set(name, value);
    }

    @Override
    public Mono<Object> setCoverage(String name, boolean forceCoverage, Object... value) {
        return valueWrapper.setCoverage(name, forceCoverage, value);
    }

    @Override
    public Flux<PropertyDescriptor> findAll() {
        return valueWrapper.findAll();
    }

    @Override
    public Flux<Tuple2<String, Object>> findTupleValues() {
        return valueWrapper.findTupleValues();
    }

    @Override
    public Object getTarget() {
        return valueWrapper.getTarget();
    }

    /**
     * 获取{@link PojoWrapper}实例。如果{@link PojoInspect#useCache()}为true，则使用缓存内的{@link PojoWrapper}
     *
     * @param pojo pojo
     * @param <T>  实体类型
     * @return PojoWrapper
     * @throws IllegalArgumentException 当通过{@link PojoInspect#isPojo(Class)}不通过时抛出
     */
    public static <T> PojoWrapper<T> getInstance(T pojo) {
        check(pojo.getClass());
        // TODO 需要考虑PojoWrapper大量创建的问题，但如果采取缓存则需要考虑加锁设置pojo实例
        return new PojoWrapper<>(pojo);
    }

    /**
     * 获取{@link PojoWrapper}实例。如果{@link PojoInspect#useCache()}为true，则使用缓存内的{@link PojoWrapper}
     *
     * @param pojoClass pojoClass
     * @param <T>       实体类型
     * @return PojoWrapper
     * @throws IllegalArgumentException 当通过{@link PojoInspect#isPojo(Class)}不通过时抛出
     */
    public static <T> PojoWrapper<T> getInstance(Class<T> pojoClass) {
        return checkPojoAndGetInstanceUseCache(pojoClass, () -> new PojoWrapper<>(pojoClass));
    }

    /**
     * 检查pojo是否合法，如果合法则获取{@link PojoWrapper}实例
     *
     * @param pojoClass pojoClass
     * @param creator   创建{@link PojoWrapper}
     * @param <T>       实例
     * @return PojoWrapper
     */
    private static <T> PojoWrapper<T> checkPojoAndGetInstanceUseCache(Class<T> pojoClass, Supplier<PojoWrapper<T>> creator) {
        PojoInspect pojoInspect = getPojoInspection(pojoClass);
        check(pojoClass);
        boolean useCache = pojoInspect.useCache();
        if (useCache) {
            return (PojoWrapper<T>) POJO_INSTANCES.computeIfAbsent(pojoClass, k -> creator.get());
        } else {
            return creator.get();
        }
    }

    private static void check(Class<?> pojoClass) {
        PojoInspect pojoInspect = getPojoInspection(pojoClass);
        boolean isPojo = pojoInspect.isPojo(pojoClass);
        if (Boolean.FALSE.equals(isPojo)) {
            throw new IllegalArgumentException(String.format("given pojo is fake, it is %s", pojoClass));
        }
    }

    private static PojoInspect getPojoInspection(Class<?> pojoClass) {
        PojoInspection pojoInspection = AnnotationUtils.findAnnotation(pojoClass, PojoInspection.class);
        return Optional.ofNullable(pojoInspection)
                .map(p -> {
                    Class<? extends PojoInspect> pojoInspectClass = p.value();
                    return POJO_INSPECTS.computeIfAbsent(pojoClass, k -> Optional.ofNullable(pojoInspectClass).map(ClassUtils::newInstance).orElse(null));
                })
                .orElse(DEFAULT_POJO_INSPECT);
    }

    /**
     * 获取Primary Key字段
     *
     * @return SQLColumnDef
     */
    public static ColumnDef findPKColumn(Class<?> pojoClass) {
        return getInstance(pojoClass).getPkColumn();
    }

    /**
     * 寻找非PK字段集
     *
     * @return 字段集合
     */
    public static List<ColumnDef> findNotPkColumns(Class<?> pojoClass) {
        return getInstance(pojoClass).getNotPkColumns();
    }

    /**
     * 获取每个POJO的表名
     *
     * @param pojoClass pojoClass
     * @return 表名
     */
    public static String findTable(Class<?> pojoClass) {
        return getInstance(pojoClass).getTable().getName().format();
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param column the column（需要驼峰）
     * @return the value
     */
    public static Object findValueByColumn(Class<?> pojoClass, String column) {
        return getInstance(pojoClass).getValueByColumn(column);
    }

    /**
     * 获取pojo的所有字段的{@link DSLName}
     *
     * @param pojoClass pojoClass
     * @return the columns
     */
    public static Collection<DSLName> findColumns(Class<?> pojoClass) {
        return getInstance(pojoClass).getColumnDSLName();
    }

    /**
     * 获取pojo的所有字段的{@link DSLName}
     *
     * @param pojoClass pojoClass
     * @return the columns
     */
    public static ColumnDef findDeleteColumn(Class<?> pojoClass) {
        return getInstance(pojoClass).getDeletedColumn();
    }

    static class DefaultPojoInspect implements PojoInspect {

        @Override
        public boolean isPojo(Class<?> maybePojo) {
            return Types.isBean(maybePojo);
        }

        @Override
        public boolean useCache() {
            return true;
        }
    }
}
