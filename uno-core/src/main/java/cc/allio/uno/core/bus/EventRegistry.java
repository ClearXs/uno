package cc.allio.uno.core.bus;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 事件注册中心
 *
 * @author j.x
 * @date 2023/4/25 13:42
 * @since 1.1.4
 */
public class EventRegistry implements Map<Class<? extends TopicEvent>, TopicEvent> {

    private final Map<Class<? extends TopicEvent>, TopicEvent> eventTables = Maps.newHashMap();

    @Override
    public int size() {
        return eventTables.size();
    }

    @Override
    public boolean isEmpty() {
        return eventTables.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return eventTables.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return eventTables.containsValue(value);
    }

    public <T extends TopicEvent> T get(Class<T> eventClass) {
        return (T) eventTables.get(eventClass);
    }

    @Override
    public TopicEvent get(Object key) {
        return eventTables.get(key);
    }

    @Override
    public TopicEvent put(Class<? extends TopicEvent> key, TopicEvent value) {
        return eventTables.put(key, value);
    }

    @Override
    public TopicEvent remove(Object key) {
        return eventTables.remove(key);
    }

    @Override
    public void putAll(Map<? extends Class<? extends TopicEvent>, ? extends TopicEvent> m) {
        eventTables.putAll(m);
    }

    @Override
    public void clear() {
        eventTables.clear();
    }

    @Override
    public Set<Class<? extends TopicEvent>> keySet() {
        return eventTables.keySet();
    }

    @Override
    public Collection<TopicEvent> values() {
        return eventTables.values();
    }

    @Override
    public Set<Entry<Class<? extends TopicEvent>, TopicEvent>> entrySet() {
        return eventTables.entrySet();
    }
}
