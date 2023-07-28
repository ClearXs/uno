package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.type.DataType;
import cc.allio.uno.data.orm.type.GenericSQLType;
import cc.allio.uno.data.orm.type.JdbcType;
import cc.allio.uno.data.orm.type.TypeRegistry;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * SQL字段定义
 *
 * @author jiangwei
 * @date 2023/4/12 19:35
 * @since 1.1.4
 */
@Data
@Builder
public class SQLColumnDef {
    // 字段名称
    private SQLName sqlName;
    // 字段注释
    private String comment;
    // 数据类型
    private DataType dataType;
    // 是否为主键
    private boolean isPk;
    // 是否为外键
    private boolean isFk;
    // 是否不为null
    private boolean isNonNull;
    // 是否为null
    private boolean isNull;
    // 是否唯一
    private boolean isUnique;

    private static final String ID = "id";

    /**
     * 根据java{@link Field}实例创建{@link SQLColumnDef}实例
     * <p>检测指定字段的值是否包含以下jpa注解</p>
     * <ul>
     *     <li>{@link Id}</li>
     *     <li>{@link Column}</li>
     * </ul>
     * <p>再检测字段是否包含mybatis注解</p>
     * <ul>
     *     <li>{@link TableId}</li>
     *     <li>{@link TableField}</li>
     * </ul>
     *
     * @param field the field
     */
    public static SQLColumnDef of(Field field) {
        SQLColumnDefBuilder builder = SQLColumnDef.builder();
        // 解析id
        Id id = field.getAnnotation(Id.class);
        if (id != null) {
            builder.isPk(true);
        }
        // 如果当前没有被@Id注解表示，尝试判断当前字段的名称是否为id
        if (id == null && (field.getName().equals(ID))) {
            builder.isPk(true);
        }
        TableId tableId = field.getAnnotation(TableId.class);
        if (tableId != null) {
            builder.isPk(true);
        }

        // 默认按照下划线进行处理
        String maybeColumnName = field.getName();
        builder.sqlName(SQLName.of(maybeColumnName, SQLName.UNDERLINE_FEATURE));

        // 解析是否包含@Column or @TableField
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            builder.isNonNull(!column.nullable());
            builder.isNull(column.nullable());
            builder.isUnique(column.unique());
            String columnName = column.name();
            if (StringUtils.isNotBlank(columnName)) {
                builder.sqlName(SQLName.of(columnName, SQLName.UNDERLINE_FEATURE));
            }
        }
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            String columnName = tableField.value();
            if (StringUtils.isNotBlank(columnName)) {
                builder.sqlName(SQLName.of(columnName, SQLName.UNDERLINE_FEATURE));
            }
        }
        // 解析该字段的类型
        Collection<JdbcType> jdbcTypes = TypeRegistry.getInstance().guessJdbcType(field.getType());
        if (CollectionUtils.isNotEmpty(jdbcTypes)) {
            JdbcType jdbcType = Lists.newArrayList(jdbcTypes).get(0);
            DataType type = DataType.create(GenericSQLType.getByJdbcCode(jdbcType.getJdbcCode()));
            builder.dataType(type);
        }
        return builder.build();
    }
}
