package cc.allio.uno.data.orm.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SQL构建过程中column
 *
 * @author jiangwei
 * @date 2023/1/5 10:46
 * @since 1.1.4
 * @deprecated 1.1.4版本删除
 */
@Data
@EqualsAndHashCode(of = {"name", "value"})
@AllArgsConstructor
@Deprecated
public abstract class RuntimeColumn {

    /**
     * 字段名称
     */
    private final String name;

    /**
     * 当前字段包含的值
     */
    private final Object[] value;

    /**
     * 字段所属于的条件
     */
    private final Condition condition;

}
