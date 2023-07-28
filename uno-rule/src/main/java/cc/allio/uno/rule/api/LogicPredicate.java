package cc.allio.uno.rule.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * logic谓词
 *
 * @author jiangwei
 * @date 2023/4/22 11:47
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum LogicPredicate {

    AND("AND", "&&"),
    OR("OR", "||");

    /**
     * 元素标识名称
     */
    @JsonValue
    private final String value;

    /**
     * 符号
     */
    private final String sm;

    /**
     * 根据指定操作服获取OP实例
     *
     * @param predicate predicate
     * @return OP实例 or null
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LogicPredicate getPredicate(String predicate) {
        for (LogicPredicate value : values()) {
            if (value.getValue().equals(predicate)) {
                return value;
            }
        }
        return null;
    }
}
