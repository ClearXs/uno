package cc.allio.uno.core.util.type;

/**
 * 类型操作
 *
 * @author jiangwei
 * @date 2021/12/23 20:06
 * @since 1.0
 */
public interface TypeOperator extends CalculateOperator {

    /**
     * 转换动作
     *
     * @param target    目标对象
     * @param maybeType 目标对象可能的类型
     * @return 转换后的对象，可能也为原对象
     */
    Object convert(Object target, Class<?> maybeType);

    /**
     * 判断数字为正数、0或负数
     *
     * @param target 目标对象
     * @return -1：负数，0：零，1：正数
     */
    int signum(Object target);

    /**
     * 返回String数据
     *
     * @return String字符串
     */
    String fromString(Object target);

    /**
     * 默认值
     *
     * @return 默认返回为null, 由实现类实现
     */
    default Object defaultValue() {
        return null;
    }
}
