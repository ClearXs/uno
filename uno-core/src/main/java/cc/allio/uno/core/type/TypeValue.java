package cc.allio.uno.core.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.bean.BeanInfoWrapper;
import cc.allio.uno.core.reflect.ParameterizedFinder;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Type-Value
 *
 * @author j.x
 * @date 2023/1/3 16:32
 * @since 1.1.4
 */
@Data
@Slf4j
public class TypeValue {

    /**
     * 值可能的类型
     */
    private final Class<?> maybeType;

    /**
     * 当前值
     */
    private final Object value;

    /**
     * 转换器
     */
    private final TypeOperator<?> maybeTypeOperator;

    private Supplier<ParameterizedFinder> parameterizedFinderSupplier;

    public TypeValue(Class<?> maybeType, Object value) {
        this(maybeType, value, TypeOperatorFactory.translator(maybeType));
    }

    public TypeValue(Class<?> maybeType, Object value, TypeOperator<?> typeOperator) {
        this.maybeType = maybeType;
        this.value = value;
        this.maybeTypeOperator = typeOperator;
    }

    public TypeValue(Class<?> maybeType, Object value, TypeOperator<?> typeOperator, Supplier<ParameterizedFinder> parameterizedFinderSupplier) {
        this.maybeType = maybeType;
        this.value = value;
        this.maybeTypeOperator = typeOperator;
        this.parameterizedFinderSupplier = parameterizedFinderSupplier;
    }

    /**
     * 以0->0对应关系对应
     *
     * @param types  Class类型
     * @param values 具体值
     * @return 多数据源
     */
    public static Flux<TypeValue> of(Class<?>[] types, Object[] values, Supplier<ParameterizedFinder> parameterizedFinderSupplier) {
        if (values == null || types.length != values.length) {
            return Flux.empty();
        }
        return Flux.zip(
                Flux.fromArray(types),
                Flux.fromIterable(Lists.newArrayList(values)),
                (t, v) -> new TypeValue(t, v, TypeOperatorFactory.translator(t), parameterizedFinderSupplier));
    }

    /**
     * 尝试按照当前存储值可能的类型转换
     * <ul>
     *     <li>强制转换</li>
     *     <li>找到当前可能类型，使用转换器转换</li>
     * </ul>
     *
     * <p>递归对类型数据进行正确的匹配，如数组、集合、map</p>
     *
     * @return 值可能正确的类型
     */
    public Object tryConvert() {
        if (Types.isBean(maybeType)) {
            return new BeanConvertor().convert(maybeType, value);
        } else if (Types.isArray(maybeType)) {
            return new ArrayConvertor().convert(maybeType, value);
        } else if (Types.isCollection(maybeType)) {
            return new CollectionConvertor(parameterizedFinderSupplier).convert(maybeType, value);
        } else if (Types.isMap(maybeType)) {
            return new MapConvertor(parameterizedFinderSupplier).convert(maybeType, value);
        } else {
            return new DirectlyConvertor(maybeTypeOperator).convert(maybeType, value);
        }
    }

    /**
     * type value transfer
     */
    interface Convertor {

        /**
         * make virtual value to actual value to type
         *
         * @param maybeType    the maybe type
         * @param virtualValue the virtual value
         * @return actual value
         */
        Object convert(Class<?> maybeType, Object virtualValue);
    }

    /**
     * through the class type cast to virtual value
     */
    static class DirectlyConvertor implements Convertor {
        private final TypeOperator<?> maybeTypeOperator;

        public DirectlyConvertor(TypeOperator<?> maybeTypeOperator) {
            this.maybeTypeOperator = maybeTypeOperator;
        }

        @Override
        public Object convert(Class<?> maybeType, Object virtualValue) {
            try {
                return maybeType.cast(virtualValue);
            } catch (ClassCastException ex) {
                if (log.isTraceEnabled()) {
                    log.trace("user translator {} getValue actual value {} type failed", maybeTypeOperator.getClass().getName(), virtualValue);
                }
                if (maybeTypeOperator != null) {
                    return maybeTypeOperator.convert(virtualValue, maybeType);
                }
            }
            return null;
        }
    }

    /**
     * convert to java bean. if value is {@link Map} value will be running. otherwise return null
     */
    static class BeanConvertor implements Convertor {

        @Override
        public Object convert(Class<?> maybeType, Object virtualValue) {
            if (virtualValue instanceof Map<?, ?>) {
                Map<?, ?> mapValue = (Map<?, ?>) virtualValue;
                Object javaObject = ClassUtils.newInstance(maybeType);
                if (javaObject == null) {
                    return null;
                }
                BeanInfoWrapper beanInfoWrapper = BeanInfoWrapper.of(maybeType);
                if (beanInfoWrapper == null) {
                    return null;
                }
                List<PropertyDescriptor> properties = beanInfoWrapper.properties();
                for (PropertyDescriptor propertyDescriptor : properties) {
                    String name = propertyDescriptor.getName();
                    Object v = mapValue.get(name);
                    if (v != null) {
                        Class<?> propertyType = propertyDescriptor.getPropertyType();
                        Object actual = new TypeValue(propertyType, v).tryConvert();
                        beanInfoWrapper.setForce(javaObject, name, actual);
                    }
                }
                return javaObject;
                // same typ
            } else if (virtualValue != null && maybeType.isAssignableFrom(virtualValue.getClass())) {
                return virtualValue;
            }
            return null;
        }
    }

    static class ArrayConvertor implements Convertor {

        @Override
        public Object convert(Class<?> maybeType, Object virtualValue) {
            Class<?> arrayType = ClassUtils.getArrayClassType(maybeType);
            if (arrayType != null) {
                // 1.如果值是String类型，则尝试以json，','分隔赋值
                if (Types.isString(virtualValue.getClass())) {
                    try {
                        List<String> arrayValues = JsonUtils.readList(virtualValue.toString(), String.class);
                        Object[] transferValues = new Object[arrayValues.size()];
                        for (int i = 0; i < arrayValues.size(); i++) {
                            transferValues[i] = new TypeValue(arrayType, arrayValues.get(i)).tryConvert();
                        }
                        return transferValues;
                    } catch (Throwable ex) {
                        // 尝试以','分隔
                        String[] arrayValues = virtualValue.toString().split(StringPool.COMMA);
                        Object[] transferValues = new Object[arrayValues.length];
                        for (int i = 0; i < arrayValues.length; i++) {
                            transferValues[i] = new TypeValue(arrayType, arrayValues[i]).tryConvert();
                        }
                        return transferValues;
                    }
                }
                // 2.如果值array类型，则直接进行转换
                if (Types.isArray(virtualValue.getClass())) {
                    Object[] arrayValues = (Object[]) virtualValue;
                    Object[] transferValues = new Object[arrayValues.length];
                    for (int i = 0; i < arrayValues.length; i++) {
                        transferValues[i] = new TypeValue(arrayType, arrayValues[i]).tryConvert();
                    }
                    return transferValues;
                }
                // 3.如果值是集合类型，进行转换
                if (Types.isList(virtualValue.getClass()) || Types.isSet(virtualValue.getClass())) {
                    Collection<Object> arrayValues = (Collection<Object>) virtualValue;
                    Object[] transferValues = new Object[arrayValues.size()];
                    Iterator<Object> iterator = arrayValues.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        transferValues[i] = new TypeValue(arrayType, iterator.next()).tryConvert();
                    }
                    return transferValues;
                }
            }
            return new Object[0];
        }
    }

    static class CollectionConvertor implements Convertor {

        private final Supplier<ParameterizedFinder> parameterizedFinderSupplier;

        public CollectionConvertor(Supplier<ParameterizedFinder> parameterizedFinderSupplier) {
            this.parameterizedFinderSupplier = parameterizedFinderSupplier;
        }

        @Override
        public Object convert(Class<?> maybeType, Object virtualValue) {
            Class<?> gType = Object.class;
            // 获取集合第一个范型类型
            if (parameterizedFinderSupplier != null) {
                ParameterizedFinder parameterizedFinder = parameterizedFinderSupplier.get();
                gType = parameterizedFinder.findFirst();
            }
            if (gType == null) {
                gType = Object.class;
            }
            Collection<Object> arrayValues = (Collection<Object>) virtualValue;
            Collection<Object> collectionValues = null;
            Iterator<Object> iterator = arrayValues.iterator();
            if (Types.isList(maybeType)) {
                collectionValues = Lists.newArrayListWithCapacity(arrayValues.size());
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    collectionValues.add(new TypeValue(gType, next).tryConvert());
                }
            }
            if (Types.isSet(maybeType)) {
                collectionValues = Sets.newHashSetWithExpectedSize(arrayValues.size());
                while (iterator.hasNext()) {
                    Object next = iterator.next();
                    collectionValues.add(new TypeValue(gType, next).tryConvert());
                }
            }
            return collectionValues;
        }
    }

    static class MapConvertor implements Convertor {

        private final Supplier<ParameterizedFinder> parameterizedFinderSupplier;

        public MapConvertor(Supplier<ParameterizedFinder> parameterizedFinderSupplier) {
            this.parameterizedFinderSupplier = parameterizedFinderSupplier;
        }

        @Override
        public Object convert(Class<?> maybeType, Object virtualValue) {
            Class<?> keyType = Object.class;
            Class<?> valueType = Object.class;
            if (parameterizedFinderSupplier != null) {
                ParameterizedFinder parameterizedFinder = parameterizedFinderSupplier.get();
                Class<?>[] actualTypes = parameterizedFinder.findAll();
                if (actualTypes.length > 1) {
                    keyType = actualTypes[0];
                    valueType = actualTypes[1];
                }
            }
            Map<Object, Object> mapValues = Maps.newHashMap();
            Map<Object, Object> transferValue = null;
            // 1.判断是否为string数据
            if (Types.isString(virtualValue.getClass())) {
                try {
                    transferValue = JsonUtils.readMap(virtualValue.toString(), Object.class, Object.class);
                } catch (Throwable ex) {
                    // ignore
                }
            }
            // 2.判断是否为map
            if (Types.isMap(virtualValue.getClass())) {
                transferValue = (Map<Object, Object>) virtualValue;
            }
            if (CollectionUtils.isNotEmpty(transferValue)) {
                for (Map.Entry<Object, Object> oEntry : transferValue.entrySet()) {
                    Object jKey = oEntry.getKey();
                    Object jValue = oEntry.getValue();
                    Object tKey = new TypeValue(keyType, jKey).tryConvert();
                    Object tValue = new TypeValue(valueType, jValue).tryConvert();
                    mapValues.put(tKey, tValue);
                }
            }
            return mapValues;
        }
    }
}
