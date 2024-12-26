package cc.allio.uno.rule.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则指标操作
 *
 * @author j.x
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum OP {
    GREATER_THAN("&gt;", ">"),
    GREATER_THAN_EQUAL("&gt;=", ">="),
    EQUAL("=", "="),
    EQUALITY("==", "=="),
    NOT_EQUAL("!=", "!="),
    LESS_THAN("&lt;", "<"),
    LESS_THAN_EQUAL("&lt;=", "<="),
    ADD("+", "+"),
    SUBTRACT("-", "-"),
    MULTIPLY("*", "*"),
    DIVIDE("/", "/"),
    NOT("~", "~"),
    NOR("^", "^"),
    CONTAINS("contains", "contains"),
    MATCHES("matches", "matches"),
    EXCLUDES("excludes", "excludes"),
    MEMBER_OF("memberOf", "memberOf"),
    INSTANCEOF("instanceof", "instanceof");

    /**
     * 操作标识（可能是转义字符）
     */
    private final String value;

    /**
     * 符号
     */
    @JsonValue
    private final String sm;

    /**
     * 根据指定操作服获取OP实例
     *
     * @param op op
     * @return OP实例 or null
     * @see #sm
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OP getOP(String op) {
        for (OP value : values()) {
            if (value.getSm().equals(op)) {
                return value;
            }
        }
        return null;
    }
}
