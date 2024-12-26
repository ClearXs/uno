package cc.allio.uno.core.env;

import org.springframework.lang.Nullable;

/**
 * 环境工具类
 *
 * @author j.x
 * @since 1.1.4
 */
public class Envs {

    private static Env env = new SystemEnv();

    /**
     * Return whether the given property key is available for resolution,
     * i.e. if the value for the given key is not {@code null}.
     */
    public static boolean containsProperty(String key) {
        return env.containsProperty(key);
    }

    /**
     * Return the property value associated with the given key,
     * or {@code null} if the key cannot be resolved.
     *
     * @param key the property name to resolve
     * @see #getProperty(String, String)
     * @see #getProperty(String, Class)
     * @see #getRequiredProperty(String)
     */
    @Nullable
    public static String getProperty(String key) {
        return env.getProperty(key);
    }

    /**
     * Return the property value associated with the given key, or
     * {@code defaultValue} if the key cannot be resolved.
     *
     * @param key          the property name to resolve
     * @param defaultValue the default value to return if no value is found
     * @see #getRequiredProperty(String)
     * @see #getProperty(String, Class)
     */
    public static String getProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }

    /**
     * Return the property value associated with the given key,
     * or {@code null} if the key cannot be resolved.
     *
     * @param key        the property name to resolve
     * @param targetType the expected type of the property value
     * @see #getRequiredProperty(String, Class)
     */
    @Nullable
    public static <T> T getProperty(String key, Class<T> targetType) {
        return env.getProperty(key, targetType);
    }

    /**
     * Return the property value associated with the given key,
     * or {@code defaultValue} if the key cannot be resolved.
     *
     * @param key          the property name to resolve
     * @param targetType   the expected type of the property value
     * @param defaultValue the default value to return if no value is found
     * @see #getRequiredProperty(String, Class)
     */
    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return env.getProperty(key, targetType, defaultValue);
    }

    /**
     * Return the property value associated with the given key (never {@code null}).
     *
     * @throws IllegalStateException if the key cannot be resolved
     * @see #getRequiredProperty(String, Class)
     */
    public static String getRequiredProperty(String key) throws IllegalStateException {
        return env.getRequiredProperty(key);
    }

    /**
     * Return the property value associated with the given key, converted to the given
     * targetType (never {@code null}).
     *
     * @throws IllegalStateException if the given key cannot be resolved
     */
    public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return env.getRequiredProperty(key, targetType);
    }

    /**
     * Resolve ${...} placeholders in the given text, replacing them with corresponding
     * property values as resolved by {@link #getProperty}. Unresolvable placeholders with
     * no default value are ignored and passed through unchanged.
     *
     * @param text the String to resolve
     * @return the resolved String (never {@code null})
     * @throws IllegalArgumentException if given text is {@code null}
     * @see #resolveRequiredPlaceholders
     */
    public static String resolvePlaceholders(String text) {
        return env.resolvePlaceholders(text);
    }

    /**
     * Resolve ${...} placeholders in the given text, replacing them with corresponding
     * property values as resolved by {@link #getProperty}. Unresolvable placeholders with
     * no default value will cause an IllegalArgumentException to be thrown.
     *
     * @return the resolved String (never {@code null})
     * @throws IllegalArgumentException if given text is {@code null}
     *                                  or if any placeholders are unresolvable
     */
    public static String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return env.resolveRequiredPlaceholders(text);
    }

    /**
     * Set property for env
     *
     * @param key      the String to key
     * @param property the String to property
     */
    public static void setProperty(String key, String property) {
        env.setProperty(key, property);
    }

    /**
     * Set property for env
     *
     * @param key      the String to key
     * @param property the T to property
     * @param <T>      the property type
     */
    public static <T> void setProperty(String key, T property) {
        env.setProperty(key, property);
    }

    /**
     * 获取当前{@link Env}的实例对象
     */
    public static Env getCurrentEnv() {
        return env;
    }

    /**
     * 重新设置current env
     *
     * @param env env
     */
    public static void reset(Env env) {
        Envs.env = env;
    }
}
