package cc.allio.uno.core.api;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定义Uno上下文模版方法
 *
 * @author j.x
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
        String objName = obj.getClass().getName();
        int objHashCode = obj.hashCode();
        String objKey = objName + StringPool.UNDERSCORE + objHashCode + StringPool.UNDERSCORE + IdGenerator.defaultGenerator().getNextIdAsString();
        putAttribute(objKey, obj);
    }

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
     * 放入新的属性数据
     *
     * @param key 属性key
     * @param obj 属性值
     * @throws NullPointerException obj为空时抛出
     */
    void putAttribute(String key, Object obj);

    /**
     * 判断是否包含key
     *
     * @param key 属性Key
     * @return 是否包含这个属性Key
     */
    default boolean containsKey(String key) {
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
     * 返回基于给定的可变餐values参数创建一个{@link ImmutableOptionalContext}
     *
     * @param values values
     * @return OptionalContext
     */
    static ImmutableOptionalContext immutable(Object... values) {
        return new ImmutableOptionalContext(values);
    }

    /**
     * 基于{@link OptionalContext}与给定的可变餐values参数创建一个{@link ImmutableOptionalContext}
     *
     * @param other  other optional context
     * @param values values
     * @return OptionalContext
     */
    static ImmutableOptionalContext immutable(OptionalContext other, Object... values) {
        return new ImmutableOptionalContext(other, values);
    }

    /**
     * 基于给定的values参数创建一个{@link ImmutableOptionalContext}
     *
     * @param values values
     * @return OptionalContext
     */
    static ImmutableOptionalContext immutable(Map<String, Object> values) {
        return new ImmutableOptionalContext(values);
    }

    /**
     * 基于{@link OptionalContext}与给定的values参数创建一个{@link ImmutableOptionalContext}
     *
     * @param other  other optional context
     * @param values values
     * @return OptionalContext
     */
    static ImmutableOptionalContext immutable(OptionalContext other, Map<String, Object> values) {
        return new ImmutableOptionalContext(other, values);
    }

    class ImmutableOptionalContext implements OptionalContext {

        private final Map<String, Object> context;
        private final AtomicInteger randomCounter = new AtomicInteger();

        public ImmutableOptionalContext(Object[] values) {
            if (values != null) {
                this.context = HashMap.newHashMap(values.length);
                for (Object value : values) {
                    putSingleValue(value);
                }
            } else {
                this.context = Collections.emptyMap();
            }
        }

        public ImmutableOptionalContext(Map<String, Object> values) {
            this.context = new HashMap<>(values);
        }

        public ImmutableOptionalContext(OptionalContext other, Map<String, Object> values) {
            this.context = new HashMap<>(other.getAll());
            this.context.putAll(values);
        }

        public ImmutableOptionalContext(OptionalContext other, Object[] values) {
            this.context = new HashMap<>(other.getAll());
            if (values != null) {
                for (Object value : values) {
                    putSingleValue(value);
                }
            }
        }

        @Override
        public Optional<Object> get(String key) {
            return Optional.ofNullable(context.get(key));
        }

        @Override
        public Optional<ApplicationContext> getApplicationContext() {
            throw Exceptions.unOperate("getApplicationContext");
        }

        @Override
        public Map<String, Object> getAll() {
            return Collections.unmodifiableMap(context);
        }

        @Override
        public void putAttribute(String key, Object obj) {
            throw Exceptions.unOperate("putAttribute");
        }

        void putSingleValue(Object value) {
            if (value != null) {
                String name = value.getClass().getName();
                this.context.put(name + randomCounter.getAndIncrement(), value);
            }
        }
    }
}
