package cc.allio.uno.data.orm.dsl;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SQL Func
 *
 * @author j.x
 * @since 1.1.4
 */
@Getter
@AllArgsConstructor
public enum Func {

    // Aggregate
    MIN_FUNCTION("MIN"),
    MAX_FUNCTION("MAX"),
    AVG_FUNCTION("AVG"),
    COUNT_FUNCTION("COUNT"),
    SUM_FUNCTION("SUM");

    // 函数名称
    private final String name;

    /**
     * 根据给定的名称获取Func实例
     *
     * @param maybeFunc func 名称
     * @return Func or null
     */
    public static Func of(String maybeFunc) {
        for (Func value : values()) {
            if (value.getName().equals(maybeFunc)) {
                return value;
            }
        }
        return null;
    }
}
