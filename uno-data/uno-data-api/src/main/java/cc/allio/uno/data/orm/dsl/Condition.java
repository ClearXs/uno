package cc.allio.uno.data.orm.dsl;

/**
 * SQL条件，如'OR'、'WHERE'...
 *
 * @author jiangwei
 * @date 2023/1/5 10:47
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
