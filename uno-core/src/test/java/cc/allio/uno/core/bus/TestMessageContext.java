package cc.allio.uno.core.bus;

import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

public class TestMessageContext implements EventContext {

    private final Map<String, Object> cache = Maps.newHashMap();

    @Override
    public Optional<Object> get(String key) {
        return Optional.empty();
    }

    @Override
    public void put(String key, Object obj) {
        cache.put(key, obj);
    }

    @Override
    public boolean remove(String key) {
        return cache.remove(key) != null;
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return Optional.empty();
    }

    @Override
    public Map<String, Object> getAll() {
        return cache;
    }

    @Override
    public String getTopicPath() {
        return "";
    }

    @Override
    public Topic<EventContext> getTopic() {
        return null;
    }

    @Override
    public EventTracer getEventTracer() {
        return null;
    }
}
