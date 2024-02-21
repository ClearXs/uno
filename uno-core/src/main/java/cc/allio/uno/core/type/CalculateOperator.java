package cc.allio.uno.core.type;

/**
 * 计算操作
 *
 * @author jiangwei
 * @date 2022/10/10 22:19
 * @since 1.1.0
 */
public interface CalculateOperator<T> {

    /**
     * 加动作
     *
     * @param origin  原始数据
     * @param passive 被加数
     */
    T add(T origin, T passive);

    /**
     * 减动作
     *
     * @param origin  原始数据
     * @param passive 被减数
     */
    T subtract(T origin, T passive);

    /**
     * 乘动作
     *
     * @param origin  原始数据
     * @param passive 被乘数
     */
    T multiply(T origin, T passive);

    /**
     * 除动作
     *
     * @param origin  原始数据
     * @param passive 被除数
     */
    T divide(T origin, T passive);
}
