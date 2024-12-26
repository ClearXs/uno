package cc.allio.uno.core.type;

/**
 * 类型操作
 *
 * @param <T> 具体操作的类型实体
 * @author j.x
 * @since 1.0
 */
public interface TypeOperator<T> extends CalculateOperator<T> {

    /**
     * 转换动作
     *
     * @param target 目标对象
     * @return 转换后的对象，可能也为原对象
     */
    default T convert(Object target) {
        return convert(target, getType());
    }

    /**
     * 转换动作
     *
     * @param target    目标对象
     * @param maybeType 目标对象可能的类型
     * @return 转换后的对象，可能也为原对象
     */
    T convert(Object target, Class<?> maybeType);

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
    default T defaultValue() {
        return null;
    }

    /**
     * 获取当前类型
     *
     * @return
     */
    Class<?> getType();
}
