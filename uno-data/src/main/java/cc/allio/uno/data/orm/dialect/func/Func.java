package cc.allio.uno.data.orm.dialect.func;

/**
 * SQL Func
 *
 * @author jiangwei
 * @date 2023/1/12 16:19
 * @since 1.1.4
 */
public interface Func {

    // Aggregate
    String MIN_FUNCTION = "min";
    String MAX_FUNCTION = "max";
    String AVG_FUNCTION = "avg";
    String COUNT_FUNCTION = "count";
    String SUM_FUNCTION = "sum";
}
