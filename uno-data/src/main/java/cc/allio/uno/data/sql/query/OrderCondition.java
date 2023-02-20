package cc.allio.uno.data.sql.query;

import cc.allio.uno.data.sql.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order condition
 *
 * @author jiangwei
 * @date 2023/1/5 10:52
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum OrderCondition implements Condition {
    // 升序
    ASC("ASC"),
    // 降序
    DESC("DESC");
    private final String name;
}
