package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order condition
 *
 * @author j.x
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
