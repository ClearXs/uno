package cc.allio.uno.core.bean;

import cc.allio.uno.core.util.ObjectUtils;
import com.google.common.collect.Maps;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * value = map的wrapper
 *
 * @author j.x
 * @since 1.1.4
 */
public class MapWrapper implements ValueWrapper {

    private final Map<String, Object> instance;

    public MapWrapper() {
        this.instance = Maps.newConcurrentMap();
    }

    public MapWrapper(Map<String, Object> value) {
        this.instance = value;
    }

    @Override
    public Boolean contains(String name) {
        return instance.containsKey(name);
    }

    @Override
    public PropertyDescriptor find(String name) {
        if (Boolean.TRUE.equals(contains(name))) {
            return find(name, instance.get(name).getClass());
        }
        return null;
    }

    @Override
    public PropertyDescriptor find(String name, Class<?> clazz) {
        if (Boolean.TRUE.equals(contains(name))) {
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(name, null, null);
                propertyDescriptor.setPropertyEditorClass(clazz);
                return propertyDescriptor;
            } catch (IntrospectionException e) {
                e.printStackTrace();
                // ignore
            }
        }
        return null;
    }

    @Override
    public <T> Mono<T> get(String name, Class<T> fieldType) {
        return Mono.justOrEmpty(instance.get(name)).cast(fieldType);
    }

    @Override
    public Mono<Object> set(String name, Object... value) {
        putMultiValues(name, value);
        return Mono.empty();
    }

    @Override
    public Mono<Object> setCoverage(String name, boolean forceCoverage, Object... value) {
        putMultiValues(name, value);
        return Mono.empty();
    }

    /**
     * map 数据存储传递多个值时处理逻辑如下:
     * <ul>
     *     <li>单值读取值实例存入</li>
     *    <li>多值作为数组存入</li>
     * </ul>
     *
     * @param name   the key
     * @param values the values myabe array
     */
    private void putMultiValues(String name, Object... values) {
        if (ObjectUtils.isNotEmpty(values)) {
            if (values.length == 1) {
                instance.put(name, values[0]);
            } else {
                instance.put(name, values);
            }
        }
    }

    @Override
    public Flux<PropertyDescriptor> findAll() {
        return Flux.fromIterable(
                instance.keySet()
                        .stream()
                        .map(this::find)
                        .toList());
    }

    @Override
    public Flux<Tuple2<String, Object>> findTupleValues() {
        return Flux.fromIterable(
                instance.entrySet().stream()
                        .map(entry -> Tuples.of(entry.getKey(), entry.getValue()))
                        .toList());
    }

    @Override
    public Object getTarget() {
        return instance;
    }
}
