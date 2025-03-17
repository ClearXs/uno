package cc.allio.uno.core.bus;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.map.OptionalMap;
import com.google.common.collect.Maps;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

/**
 * EventBus上下文对象
 *
 * @author j.x
 * @since 1.1.2
 */
public class DefaultEventContext implements EventContext {

    protected final Map<String, Object> attributes;

    // ========================= CONSTANT =========================
    public static final String APPLICATION_CONTEXT_KEY = "application_context";
    public static final String EVENT_TRACER_KEY = "event_tracer";

    public DefaultEventContext() {
        this(Maps.newConcurrentMap());
    }

    public DefaultEventContext(OptionalMap<String> optionalContext) {
        this(optionalContext.getAll());
    }

    public DefaultEventContext(Map<String, Object> attributes) {
        this(attributes, null);
    }

    public DefaultEventContext(@NonNull Map<String, Object> attributes, ApplicationContext applicationContext) {
        this.attributes = Maps.newConcurrentMap();
        this.attributes.putAll(attributes);
        if (applicationContext != null) {
            this.attributes.put(APPLICATION_CONTEXT_KEY, applicationContext);
        }
        this.attributes.put(EVENT_TRACER_KEY, new EventTracer());
    }

    public DefaultEventContext(DefaultEventContext messageContext) {
        this.attributes = messageContext.attributes;
    }

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(attributes.get(key));
    }

    @Override
    public void put(String key, Object obj) {
        attributes.put(key, obj);
    }

    @Override
    public boolean remove(String s) {
        return attributes.remove(s) != null;
    }

    @Override
    public Optional<ApplicationContext> getApplicationContext() {
        return get(APPLICATION_CONTEXT_KEY, ApplicationContext.class);
    }

    @Override
    public Map<String, Object> getAll() {
        return attributes;
    }

    @Override
    public String getTopicPath() {
        return get(TOPIC_PATH_KEY, String.class).orElse(StringPool.EMPTY);
    }

    @Override
    public Topic<?> getTopic() {
        return get(TOPIC_KEY, Topic.class).orElse(null);
    }

    @Override
    public EventTracer getEventTracer() {
        return get(EVENT_TRACER_KEY, EventTracer.class).orElse(null);
    }

}
