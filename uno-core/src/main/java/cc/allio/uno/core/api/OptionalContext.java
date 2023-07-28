package cc.allio.uno.core.api;

import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * 定义Uno上下文模版方法
 *
 * @author jiangwei
 * @date 2022/3/30 14:08
 * @since 1.0.6
 */
public interface OptionalContext {

    /**
     * 获取指定Key的数据
     *
     * @param key 属性key
     * @return instance or null
     */
    default Object getForce(String key) {
        return get(key).orElse(null);
    }

    /**
     * 获取指定Key的数据
     *
     * @param key 属性key
     * @return Optional实例对象，如果属性不存在则返回空
     */
    Optional<Object> get(String key);

    /**
     * 按照指定Key和类型获取指定的数据
     *
     * @param key   指定的Key
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return instance or null
     * @throws ClassCastException 当类型转换错误时抛出
     */
    default <T> T getForce(String key, Class<T> clazz) {
        return get(key).map(clazz::cast).orElse(null);
    }

    /**
     * 按照指定Key和类型获取指定的数据
     *
     * @param key   指定的Key
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return Option
     * @throws ClassCastException 当类型转换错误时抛出
     */
    default <T> Optional<T> get(String key, Class<T> clazz) {
        return get(key).map(clazz::cast);
    }

    /**
     * 放入新的属性数据
     *
     * @param key 属性key
     * @param obj 属性值
     * @throws NullPointerException obj为空时抛出
     */
    void putAttribute(String key, Object obj);

    /**
     * 放入其他所有的属性数据
     *
     * @param otherAttributes 其他属性数据
     */
    default void putAll(Map<String, Object> otherAttributes) {
        for (Map.Entry<String, Object> attribute : otherAttributes.entrySet()) {
            putAttribute(attribute.getKey(), attribute.getValue());
        }
    }

    /**
     * 获取Spring应用的上下文
     *
     * @return 返回Spring上下文实例
     * @throws NullPointerException 获取不到时抛出
     */
    Optional<ApplicationContext> getApplicationContext();

    /**
     * 获取所有key-value
     *
     * @return key-value
     */
    Map<String, Object> getAll();

    /**
     * 获取目标对象数据或者抛出异常
     *
     * @param key  属性Key
     * @param type 目标Class对象
     * @param <T>  范型数据
     * @return 获取的目标对象
     * @throws NullPointerException 如果目标对象不存在则抛出该异常
     * @throws ClassCastException   如果目标对象的类型不是给定的类型则抛出该异常
     */
    default <T> T getOrThrows(String key, Class<T> type) {
        Optional<Object> targetOptional = get(key);
        Object target = targetOptional.orElseThrow(() -> new NullPointerException(String.format("Can't Get %s Object dose not exist", key)));
        return type.cast(target);
    }

    /**
     * 判断是否包含key
     *
     * @param key 属性Key
     * @return 是否包含这个属性Key
     */
    default boolean containsKey(String key) {
        return get(key).isPresent();
    }
}
