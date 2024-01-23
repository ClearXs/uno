package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.FieldUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslator;
import cc.allio.uno.data.orm.dsl.dialect.TypeTranslatorHolder;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.orm.dsl.type.JdbcType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.TypeRegistry;
import com.google.common.collect.Lists;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * 定义Pojo与SQL相关的操作
 * 注：pojo的字段驼峰
 *
 * @author jiangwei
 * @date 2023/7/4 17:14
 * @since 1.1.4
 */
public class PojoWrapper<T> extends ObjectWrapper {

    private final T pojo;
    private final Class<?> pojoClass;
    private final List<Field> pojoFields;
    private final List<ColumnDef> columnDefs;
    private static final ExpressionTemplate DOLLAR_TEMPLATE = ExpressionTemplate.createTemplate(Tokenizer.DOLLAR_BRACE);
    private static final String ID = "id";

    public PojoWrapper(T pojo) {
        super(pojo);
        checkPojo(pojo);
        this.pojo = pojo;
        this.pojoClass = pojo.getClass();
        this.pojoFields = FieldUtils.getAllFieldsList(pojoClass);
        this.columnDefs = getSQLColumnDef();
    }

    public PojoWrapper(Class<?> pojoClass) {
        super(pojoClass);
        this.pojo = null;
        this.pojoClass = pojoClass;
        this.pojoFields = FieldUtils.getAllFieldsList(pojoClass);
        this.columnDefs = getSQLColumnDef();
    }

    private void checkPojo(Object maybePojo) {
        if (maybePojo == null) {
            throw new IllegalArgumentException("pojo is null");
        }
        if (!Types.isBean(maybePojo.getClass())) {
            throw new IllegalArgumentException(String.format("given pojo is fake, it is %s", maybePojo.getClass()));
        }
    }

    /**
     * 获取Primary Key字段
     *
     * @return SQLColumnDef
     */
    public static ColumnDef findPKColumn(Class<?> pojoClass) {
        return new PojoWrapper<>(pojoClass).getPKColumn();
    }

    /**
     * 获取Primary Key字段
     *
     * @return SQLColumnDef
     */
    public ColumnDef getPKColumn() {
        return columnDefs.stream()
                .filter(ColumnDef::isPk)
                .findFirst()
                .orElseThrow(() -> new DSLException(String.format("the class %s not found id", pojoClass)));
    }

    /**
     * 获取Primary Key字段值
     */
    public Object getPKValue() {
        ColumnDef id = getPKColumn();
        return getForce(id.getDslName().format());
    }

    /**
     * 寻找非PK字段集
     *
     * @return 字段集合
     */
    public static List<ColumnDef> findNotPkColumns(Class<?> pojoClass) {
        return new PojoWrapper<>(pojoClass).getNotPkColumns();
    }

    /**
     * 寻找非PK字段集
     *
     * @return 字段集合
     */
    public List<ColumnDef> getNotPkColumns() {
        return columnDefs.stream()
                .filter(c -> !c.isPk())
                .toList();
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param column the column（需要驼峰）
     * @return the value
     */
    public static Object findValueByColumn(Class<?> pojoClass, String column) {
        return new PojoWrapper<>(pojoClass).getValueByColumn(column);
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param column the column（需要驼峰）
     * @return the value
     */
    public Object getValueByColumn(String column) {
        return getForce(column);
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
     * 获取每个POJO的表名
     *
     * @param pojoClass pojoClass
     * @return 表名
     */
    public static String findTable(Class<?> pojoClass) {
        return new PojoWrapper<>(pojoClass).getTable().getName().format();
    }

    /**
     * 获取该Pojo对象的表名
     *
     * @return Table instance
     */
    public Table getTable() {
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
    }

    /**
     * 获取每个pojo的所有字段
     *
     * @param pojoClass pojoClass
     * @return the columns
     */
    public static List<DSLName> findColumns(Class<?> pojoClass) {
        return new PojoWrapper<>(pojoClass).getSQLColumnDef().stream().map(ColumnDef::getDslName).toList();
    }

    /**
     * 获取该pojo字段的解析的sql字段
     */
    public List<ColumnDef> getSQLColumnDef() {
        return pojoFields.stream()
                .map(this::createColumnDef)
                .toList();
    }

    /**
     * 根据java{@link Field}实例创建{@link ColumnDef}实例
     * <p>检测指定字段的值是否包含以下jpa注解</p>
     * <ul>
     *     <li>{@link Id}</li>
     *     <li>{@link Column}</li>
     * </ul>
     *
     * @param field the field
     */
    private ColumnDef createColumnDef(Field field) {
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
            // TODO 考虑如何做到由java类型最佳匹配jdbc类型
            JdbcType jdbcType = Lists.newArrayList(jdbcTypes).get(0);
            DSLType guessSQLType = DSLType.getByJdbcCode(jdbcType.getJdbcCode());
            TypeTranslator translator = TypeTranslatorHolder.getTypeTranslator();
            DSLType sqlType = translator.translate(guessSQLType);
            DataType type = DataType.create(sqlType);
            builder.dataType(type);
        }
        return builder.build();
    }

    public T getPojoValue() {
        return pojo;
    }
}
