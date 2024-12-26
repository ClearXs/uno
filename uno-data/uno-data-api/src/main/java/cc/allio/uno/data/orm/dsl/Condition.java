package cc.allio.uno.data.orm.dsl;

/**
 * SQL条件，如'OR'、'WHERE'...
 *
 * @author j.x
 * @since 1.1.4
 */
public interface Condition {

    /**
     * 获取条件名称
     *
     * @return 条件名称
     */
    String getName();
}
