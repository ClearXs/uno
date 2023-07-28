package cc.allio.uno.core.bus;

import cc.allio.uno.core.api.OptionalContext;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

public class TestMessageContext implements OptionalContext {

    private final Map<String, Object> cache = Maps.newHashMap();

    @Override
    public Optional<Object> get(String key) {
        return Optional.empty();
    }

    @Override
    public void putAttribute(String key, Object obj) {
        cache.put(key, obj);
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getAll() {
        return cache;
    }
}
