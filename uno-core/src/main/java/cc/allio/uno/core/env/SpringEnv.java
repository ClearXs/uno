package cc.allio.uno.core.env;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

/**
 * 基于Spring{@link Environment}的Env
 *
 * @author jiangwei
 * @date 2023/4/27 14:10
 * @since 1.1.4
 */
public class SpringEnv implements Env {

    private final ConfigurableEnvironment environment;
    private final SystemEnv systemEnv;
    public static final String PROPERTY_SOURCE_KEY = "system-env";

    public SpringEnv(SystemEnv systemEnv, ConfigurableEnvironment environment) {
        this.systemEnv = systemEnv;
        this.environment = environment;
        reload();
    }

    @Override
    public boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return null;
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return environment.getRequiredProperty(key, targetType);
    }

    @Override
    public String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return environment.resolveRequiredPlaceholders(text);
    }

    @Override
    public void setProperty(String key, String property) {
        systemEnv.setProperty(key, property);
        reload();
    }

    @Override
    public <T> void setProperty(String key, T property) {
        systemEnv.setProperty(key, property);
        reload();
    }

    /**
     * 重新加载 property source
     */
    private void reload() {
        if (environment.getPropertySources().contains(PROPERTY_SOURCE_KEY)) {
            environment.getPropertySources().remove(PROPERTY_SOURCE_KEY);
        }
        MapPropertySource mapPropertySource = new MapPropertySource("environment.getPropertySources()", systemEnv.getGlobalProperties());
        environment.getPropertySources().addLast(mapPropertySource);
    }
}
