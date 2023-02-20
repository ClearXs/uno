package cc.allio.uno.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OP {
    GREATER_THAN("&gt;", ">"),
    GREATER_THAN_EQUAL("&gt;=", ">="),
    EQUAL("=", "="),
    NOT_EQUAL("!=", "!="),
    LESS_THAN("&lt;", "<"),
    LESS_THAN_EQUAL("&lt;=", "<="),
    ADD("+", "+"),
    SUBTRACT("-", "-"),
    MULTIPLY("*", "*"),
    DIVIDE("/", "/"),
    AND("&", "&"),
    OR("|", "|"),
    NOT("~", "~"),
    NOR("^", "^");

    /**
     * 操作标识（可能是转义字符）
     */
    private final String op;

    /**
     * 符号
     */
    private final String symbol;

    /**
     * 根据指定操作服获取OP实例
     *
     * @param op op
     * @return OP实例 or null
     */
    public static OP getOP(String op) {
        for (OP value : values()) {
            if (value.getOp().equals(op)) {
                return value;
            }
        }
        return null;
    }
}
