package cc.allio.uno.core.util.map;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;

import java.util.*;

/**
 * the optional map.
 * <p>
 * existing following features:
 *
 * <ul>
 *     <li>all get method return {@link Optional} object such as {@link #get(Object, Class)}</li>
 *     <li>support class cast value operation</li>
 * </ul>
 *
 * @param <Key> the map key type
 * @author j.x
 * @since 1.0.6
 */
public interface OptionalMap<Key> {

    /**
     * 获取指定Key的数据
     *
     * @param key 属性key
     * @return instance or null
     */
    default Object getForce(Key key) {
        return get(key).orElse(null);
    }

    /**
     * 获取指定Key的数据
     *
     * @param key 属性key
     * @return Optional实例对象，如果属性不存在则返回空
     */
    Optional<Object> get(Key key);

    /**
     * 按照指定Key和类型获取指定的数据
     *
     * @param key   指定的Key
     * @param clazz 类型
     * @param <T>   类型泛型
     * @return instance or null
     * @throws ClassCastException 当类型转换错误时抛出
     */
    default <T> T getForce(Key key, Class<T> clazz) {
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
    default <T> Optional<T> get(Key key, Class<T> clazz) {
        return get(key).map(clazz::cast);
    }

    /**
     * get list by key
     *
     * @param key          the key
     * @param elementClass the element {@link Class}
     * @param <T>          the element generic type
     * @return the optional for list
     */
    default <T> Optional<List<T>> getList(Key key, Class<T> elementClass) {
        return get(key)
                .flatMap(obj -> {
                    if (obj instanceof List<?> oldList) {
                        List<T> list = Lists.newArrayList();
                        for (Object o : oldList) {
                            if (ClassUtils.isAssignable(elementClass, o.getClass())) {
                                list.add(elementClass.cast(o));
                            }
                        }
                        return Optional.of(list);
                    }
                    return Optional.empty();
                });
    }

    /**
     * get map by key
     *
     * @param key        the key
     * @param keyClass   the map key {@link Class}
     * @param valueClass the value key {@link Class}
     * @param <K>        the key generic type
     * @param <V>        the value generic type
     * @return the optional for map
     */
    default <K, V> Optional<Map<K, V>> getMap(Key key, Class<K> keyClass, Class<V> valueClass) {
        return get(key)
                .flatMap(obj -> {
                    if (obj instanceof Map<?, ?> map) {
                        Map<K, V> newMap = Maps.newHashMap();
                        for (Map.Entry<?, ?> entry : map.entrySet()) {
                            Object k = entry.getKey();
                            Object v = entry.getValue();
                            if (ClassUtils.isAssignable(keyClass, k.getClass())
                                    && ClassUtils.isAssignable(valueClass, v.getClass())) {
                                newMap.put(keyClass.cast(k), valueClass.cast(v));
                            }
                        }
                        return Optional.of(newMap);
                    }
                    return Optional.empty();
                });
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
    Map<Key, Object> getAll();

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
    default <T> T getOrThrows(Key key, Class<T> type) {
        Optional<Object> targetOptional = get(key);
        Object target = targetOptional.orElseThrow(() -> new NullPointerException(String.format("Can't Get %s Object dose not exist", key)));
        return type.cast(target);
    }

    /**
     * 给定一个类型，获取第一个匹配的属性
     *
     * @param typeClass typeClass
     * @param <T>       类型
     * @return option
     */
    default <T> Optional<T> getTypeFirst(Class<T> typeClass) {
        return getValues()
                .stream()
                .filter(value -> typeClass.isAssignableFrom(value.getClass()))
                .findFirst()
                .map(typeClass::cast);
    }

    /**
     * 如果没有获取到则返回null
     *
     * @see #getTypeFirst(Class)
     */
    default <T> T getTypeFirstForce(Class<T> typeClass) {
        return getTypeFirst(typeClass).orElse(null);
    }

    /**
     * according to types get corresponding obj.
     * <b>you should invoke before invoke {@link #matchAll(Class[])}</b>
     *
     * @return obj
     * @throws IllegalArgumentException not find any one type obj throws
     */
    default Object[] getAllType(Class<?>[] types) {
        List<Object> objs = Lists.newArrayList();
        for (Class<?> type : types) {
            Object obj = getTypeFirstForce(type);
            if (obj == null) {
                throw new IllegalArgumentException(String.format("given type %s not found obj", type.getName()));
            }
            objs.add(obj);
        }
        return objs.toArray(Object[]::new);
    }

    /**
     * 随机生成Key进行放入
     *
     * @param obj 属性值
     * @throws NullPointerException obj为空时抛出
     */
    default void put(Object obj) {
        Key key = randomKey();
        put(key, obj);
    }

    /**
     * 放入其他所有的属性数据
     *
     * @param otherMap the other map
     */
    default void putAll(OptionalMap<Key> otherMap) {
        for (Map.Entry<Key, Object> attribute : otherMap.getAll().entrySet()) {
            put(attribute.getKey(), attribute.getValue());

        }
    }

    /**
     * 放入其他所有的属性数据
     *
     * @param otherAttributes 其他属性数据
     */
    default void putAll(Map<Key, Object> otherAttributes) {
        for (Map.Entry<Key, Object> attribute : otherAttributes.entrySet()) {
            put(attribute.getKey(), attribute.getValue());
        }
    }

    /**
     * 放入新的属性数据
     *
     * @param key 属性key
     * @param obj 属性值
     * @throws NullPointerException obj为空时抛出
     */
    void put(Key key, Object obj);

    /**
     * remove value from map
     *
     * @param key the key
     * @return true if success.
     */
    boolean remove(Key key);

    /**
     * 判断是否包含key
     *
     * @param key 属性Key
     * @return 是否包含这个属性Key
     */
    default boolean containsKey(Key key) {
        return get(key).isPresent();
    }

    /**
     * given type array decide whether match all
     *
     * @param types the array type
     * @return match if true
     */
    default boolean matchAll(Class<?>[] types) {
        if (types == null) {
            return false;
        }
        if (types.length == 0 && size() == 0) {
            return true;
        } else if (types.length == 0 && size() > 0) {
            return false;
        } else {
            return Arrays.stream(types).allMatch(this::match);
        }
    }

    /**
     * given a type decide whether match
     *
     * @param type the type
     * @return match if true
     */
    default boolean match(Class<?> type) {
        return getValues()
                .stream()
                .anyMatch(p -> {
                    Class<?> leftHand = p.getClass();
                    if (leftHand.isInterface()) {
                        return ClassUtils.isAssignable(leftHand, type);
                    } else if (type.isInterface()) {
                        return ClassUtils.isAssignable(type, leftHand);
                    }
                    return leftHand.isNestmateOf(type);
                });
    }

    /**
     * return the optional context store data length
     *
     * @return the number of data length
     */
    default int size() {
        return getAll().size();
    }

    /**
     * return the value view container in optional context. reference to {@link Map#values()}
     *
     * @return the {@link Collection} values
     */
    default Collection<Object> getValues() {
        return getAll().values();
    }

    /**
     * return generic type {@code Key} {@link Class} instance
     */
    default Class<Key> getKeyType() {
        return (Class<Key>) ReflectTools.getGenericType(this, OptionalMap.class, 0);
    }

    /**
     * return random {@code Key} value
     *
     * @return the key value
     * @throws ClassCastException
     * @throws UnsupportedOperationException
     */
    default Key randomKey() {
        Class<Key> type = getKeyType();
        if (type == null) {
            type = (Class<Key>) String.class;
        }
        if (String.class.isAssignableFrom(type)) {
            return (Key) IdGenerator.defaultGenerator().getNextIdAsString();
        } else if (Long.class.isAssignableFrom(type)) {
            return (Key) IdGenerator.defaultGenerator().getNextIdAsString();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * of new {@link OptionalMap}
     */
    static <Key> OptionalMap<Key> of() {
        return new OptionalHashMap<>();
    }

    /**
     * of new {@link OptionalMap} from other map
     */
    static <Key> OptionalMap<Key> of(OptionalHashMap<Key> otherMap) {
        OptionalHashMap<Key> map = new OptionalHashMap<>();
        map.putAll(otherMap);
        return map;
    }

    /**
     * of new {@link OptionalMap} from other map
     */
    static <Key> OptionalMap<Key> of(Map<Key, Object> otherMap) {
        OptionalHashMap<Key> map = new OptionalHashMap<>();
        map.putAll(otherMap);
        return map;
    }

    /**
     * 返回基于给定的可变餐values参数创建一个{@link ImmutableOptionalMap}
     *
     * @param values values
     * @return OptionalContext
     */
    static <Key> OptionalMap<Key> immutable(Object... values) {
        return new ImmutableOptionalMap<>(values);
    }

    /**
     * 基于{@link OptionalMap}与给定的可变餐values参数创建一个{@link ImmutableOptionalMap}
     *
     * @param other  other optional context
     * @param values values
     * @return OptionalContext
     */
    static <Key> OptionalMap<Key> immutable(OptionalMap<Key> other, Object... values) {
        return new ImmutableOptionalMap<>(other, values);
    }

    /**
     * 基于给定的values参数创建一个{@link ImmutableOptionalMap}
     *
     * @param values values
     * @return OptionalContext
     */
    static <Key> OptionalMap<Key> immutable(Map<Key, Object> values) {
        return new ImmutableOptionalMap<>(values);
    }

    /**
     * 基于{@link OptionalMap}与给定的values参数创建一个{@link ImmutableOptionalMap}
     *
     * @param other  other optional context
     * @param values values
     * @return OptionalContext
     */
    static <Key> OptionalMap<Key> immutable(OptionalMap<Key> other, Map<Key, Object> values) {
        return new ImmutableOptionalMap<>(other, values);
    }

    class OptionalHashMap<Key> implements OptionalMap<Key> {

        private final Map<Key, Object> context = Maps.newHashMap();

        @Override
        public Optional<Object> get(Object key) {
            return Optional.ofNullable(context.get(key));
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<Key, Object> getAll() {
            return context;
        }

        @Override
        public void put(Key key, Object obj) {
            context.put(key, obj);
        }

        @Override
        public boolean remove(Key key) {
            return context.remove(key) != null;
        }
    }

    class ImmutableOptionalMap<Key> implements OptionalMap<Key> {

        private final Map<Key, Object> context;

        public ImmutableOptionalMap(Object[] values) {
            if (values != null) {
                this.context = HashMap.newHashMap(values.length);
                for (Object value : values) {
                    putSingleValue(value);
                }
            } else {
                this.context = Collections.emptyMap();
            }
        }

        public ImmutableOptionalMap(Map<Key, Object> values) {
            this.context = new HashMap<>(values);
        }

        public ImmutableOptionalMap(OptionalMap<Key> other, Map<Key, Object> values) {
            this.context = new HashMap<>(other.getAll());
            this.context.putAll(values);
        }

        public ImmutableOptionalMap(OptionalMap<Key> other, Object[] values) {
            this.context = new HashMap<>(other.getAll());
            if (values != null) {
                for (Object value : values) {
                    putSingleValue(value);
                }
            }
        }

        @Override
        public Optional<Object> get(Key key) {
            return Optional.ofNullable(context.get(key));
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            throw Exceptions.unOperate("getApplicationContext");
        }

        @Override
        public Map<Key, Object> getAll() {
            return Collections.unmodifiableMap(context);
        }

        @Override
        public void put(Key key, Object obj) {
            throw Exceptions.unOperate("put");
        }

        @Override
        public boolean remove(Key key) {
            throw Exceptions.unOperate("remove");
        }

        void putSingleValue(Object value) {
            if (value != null) {
                Key name = randomKey();
                this.context.put(name, value);
            }
        }
    }
}
