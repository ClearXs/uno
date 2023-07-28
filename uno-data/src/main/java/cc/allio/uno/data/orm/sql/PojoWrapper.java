package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.ReflectUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import cc.allio.uno.data.orm.type.JavaType;
import cc.allio.uno.data.orm.type.StringJavaType;
import cc.allio.uno.data.orm.type.TypeRegistry;
import org.springframework.core.annotation.AnnotationUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private final List<SQLColumnDef> columnDefs;
    private static final ExpressionTemplate DOLLAR_TEMPLATE = ExpressionTemplate.createTemplate(Tokenizer.DOLLAR_BRACE);

    /**
     * 常量字段
     */
    private static final String CREATE_TIME = "createTime";
    private static final FieldSetValueStrategy CREATE_TIME_STRATEGY = (p, f, t) -> p.setForce(f, new Date());
    private static final String UPDATE_TIME = "updateTime";
    private static final FieldSetValueStrategy UPDATE_TIME_STRATEGY = (p, f, t) -> p.setForce(f, new Date());
    private static final String IS_DELETED = "isDeleted";
    private static final FieldSetValueStrategy IS_DELETED_STRATEGY = (p, f, t) -> p.setForce(f, 0);

    public PojoWrapper(T pojo) {
        super(pojo);
        checkPojo(pojo);
        this.pojo = pojo;
        this.pojoClass = pojo.getClass();
        this.pojoFields = ReflectUtils.getAllField(pojoClass);
        this.columnDefs = getSQLColumnDef();
    }

    public PojoWrapper(Class<?> pojoClass) {
        super(pojoClass);
        this.pojo = null;
        this.pojoClass = pojoClass;
        this.pojoFields = ReflectUtils.getAllField(pojoClass);
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
     * 获取被{@link Id}注释的字段
     *
     * @return SQLColumnDef
     */
    public SQLColumnDef findByIdColumn() {
        return columnDefs.stream()
                .filter(SQLColumnDef::isPk)
                .findFirst()
                .orElseThrow(() -> new SQLException(String.format("the class %s not found id", pojoClass)));
    }

    /**
     * 获取被{@link Id}注释的字段的值
     */
    public Object findByIdValue() {
        SQLColumnDef id = findByIdColumn();
        return getForce(id.getSqlName().format());
    }

    /**
     * 根据column名称获取对应的数据
     *
     * @param column the column（需要驼峰）
     * @return the value
     */
    public Object findByColumnValue(String column) {
        return getForce(column);
    }

    /**
     * 获取该Pojo对象的表名
     *
     * @return Table instance
     */
    public Table getTable() {
        // 取jpa注解
        javax.persistence.Table table = AnnotationUtils.findAnnotation(pojoClass, javax.persistence.Table.class);
        String indexName = StringPool.EMPTY;
        if (table != null) {
            indexName = table.name();
        }
        if (StringUtils.isEmpty(indexName)) {
            // 取类名并转换为下划线命名
            indexName = SQLName.of(pojoClass.getSimpleName(), SQLName.UNDERLINE_FEATURE).format();
        }
        if (pojo != null) {
            indexName = DOLLAR_TEMPLATE.parseTemplate(indexName, findAllValuesForce());
        }
        return Table.of(indexName);
    }

    /**
     * 获取该pojo字段的解析的sql字段
     */
    public List<SQLColumnDef> getSQLColumnDef() {
        return pojoFields.stream()
                .map(SQLColumnDef::of)
                .collect(Collectors.toList());
    }

    /**
     * 此方法返回经过策略调整后的值。包含以下：
     * <ul>
     *     <li>{@link Id}的赋值</li>
     *     <li>{@link #CREATE_TIME}、{@link #UPDATE_TIME}、{@link #IS_DELETED}赋值</li>
     * </ul>
     */
    public T getPojoValue() {
        for (SQLColumnDef columnDef : columnDefs) {
            JavaType<?> javaType;
            try {
                int jdbcType = columnDef.getDataType().getSqlType().getJdbcType();
                javaType = TypeRegistry.getInstance().findJavaType(jdbcType);
            } catch (Throwable ex) {
                // ignore
                javaType = new StringJavaType();
            }
            // 主键
            if (columnDef.isPk()) {
                new IdStrategy().setValue(this, columnDef.getSqlName().format(SQLName.PLAIN_FEATURE), javaType.getJavaType());
            }
            // 设置默认值
            if (CREATE_TIME.equals(columnDef.getSqlName().format(SQLName.PLAIN_FEATURE))) {
                CREATE_TIME_STRATEGY.setValue(this, CREATE_TIME, javaType.getJavaType());
            }
            if (UPDATE_TIME.equals(columnDef.getSqlName().format(SQLName.PLAIN_FEATURE))) {
                UPDATE_TIME_STRATEGY.setValue(this, UPDATE_TIME, javaType.getJavaType());
            }
            if (IS_DELETED.equals(columnDef.getSqlName().format(SQLName.PLAIN_FEATURE))) {
                IS_DELETED_STRATEGY.setValue(this, IS_DELETED, javaType.getJavaType());
            }
        }
        return pojo;
    }

    /**
     * 字段设置值策略
     */
    interface FieldSetValueStrategy {

        /**
         * 设置值
         *
         * @param pojoWrapper the pojoWrapper
         * @param fieldName
         * @param fieldType   the field
         */
        void setValue(PojoWrapper<?> pojoWrapper, String fieldName, Class<?> fieldType);
    }

    /**
     * id值插入策略，当字段存在{@link Id}。如果值不为空则设置{@link IdGenerator}
     */
    static class IdStrategy implements FieldSetValueStrategy {

        @Override
        public void setValue(PojoWrapper<?> pojoWrapper, String fieldName, Class<?> fieldType) {
            Object r = pojoWrapper.getForce(fieldType.getName());
            if (r == null) {
                if (Types.isLong(fieldType)) {
                    Long nextId = IdGenerator.defaultGenerator().getNextId();
                    pojoWrapper.setForce(fieldName, nextId);
                } else if (Types.isString(fieldType)) {
                    String nextIdAsString = IdGenerator.defaultGenerator().getNextIdAsString();
                    pojoWrapper.setForce(fieldType.getName(), nextIdAsString);
                }
            }
        }
    }
}
