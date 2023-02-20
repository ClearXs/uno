package cc.allio.uno.data.sql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SQL构建过程中column
 *
 * @author jiangwei
 * @date 2023/1/5 10:46
 * @since 1.1.4
 */
@Data
@EqualsAndHashCode(of = {"name", "value"})
@AllArgsConstructor
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
