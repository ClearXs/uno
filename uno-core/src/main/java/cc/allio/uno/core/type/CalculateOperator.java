package cc.allio.uno.core.type;

/**
 * 计算操作
 *
 * @author jiangwei
 * @date 2022/10/10 22:19
 * @since 1.1.0
 */
public interface CalculateOperator {

    /**
     * 加动作
     *
     * @param origin  原始数据
     * @param passive 被加数
     */
    Object add(Object origin, Object passive);

    /**
     * 减动作
     *
     * @param origin  原始数据
     * @param passive 被减数
     */
    Object subtract(Object origin, Object passive);

    /**
     * 乘动作
     *
     * @param origin  原始数据
     * @param passive 被乘数
     */
    Object multiply(Object origin, Object passive);

    /**
     * 除动作
     *
     * @param origin  原始数据
     * @param passive 被除数
     */
    Object divide(Object origin, Object passive);
}
