package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.data.orm.dsl.type.DataType;
import lombok.Builder;
import lombok.Data;


/**
 * SQL字段定义
 *
 * @author jiangwei
 * @date 2023/4/12 19:35
 * @since 1.1.4
 */
@Data
@Builder
public class ColumnDef {
    // 字段名称
    private DSLName dslName;
    // 字段注释
    private String comment;
    // 数据类型
    private DataType dataType;
    // 默认值
    private Object defaultValue;
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
}
