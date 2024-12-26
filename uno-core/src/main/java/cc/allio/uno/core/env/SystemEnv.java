package cc.allio.uno.core.env;

import cc.allio.uno.core.util.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 基于本地系统默认实现
 *
 * @author j.x
 * @since 1.1.4
 */
public class SystemEnv implements Env {

    private static final Properties GLOBAL_PROPERTIES = new Properties();

    @Override
    public boolean containsProperty(String key) {
        return GLOBAL_PROPERTIES.containsKey(key);
    }

    @Override
    public String getProperty(String key) {
        return GLOBAL_PROPERTIES.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return GLOBAL_PROPERTIES.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        throw new UnsupportedOperationException("system env unsupported");
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        throw new UnsupportedOperationException("system env unsupported");
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        String property = getProperty(key);
        if (StringUtils.isEmpty(property)) {
            throw new IllegalStateException(String.format("property key %s is empty", key));
        }
        return property;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        throw new UnsupportedOperationException("system env unsupported");
    }

    @Override
    public String resolvePlaceholders(String text) {
        throw new UnsupportedOperationException("system env unsupported");
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("system env unsupported");
    }

    @Override
    public void setProperty(String key, String property) {
        GLOBAL_PROPERTIES.put(key, property);
    }

    @Override
    public <T> void setProperty(String key, T property) {
        throw new UnsupportedOperationException("system env unsupported");
    }

    public Map<String, Object> getGlobalProperties() {
        return GLOBAL_PROPERTIES.entrySet().stream().collect(Collectors.toMap(k -> (String) k.getKey(), Map.Entry::getValue));
    }
}
