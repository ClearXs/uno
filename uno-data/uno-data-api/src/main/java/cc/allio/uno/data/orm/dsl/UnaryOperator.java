package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SQL 一元操作符
 *
 * @author j.x
 * @date 2023/2/27 19:04
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum UnaryOperator {
    PLUS("+"),
    NEGATIVE("-"),
    not("!"),
    COMPL("~"),
    PRIOR("PRIOR"),
    CONNECT_BY_ROOT("CONNECT BY"),
    BINARY("BINARY"),
    RAW("RAW"),
    NOT("NOT"),
    // Number of points in path or polygon
    Pound("#");
    private final String name;
}
