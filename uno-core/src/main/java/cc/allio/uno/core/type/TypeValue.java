package cc.allio.uno.core.type;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Type-Value
 *
 * @author jiangwei
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
    private final TypeOperator maybeTypeOperator;

    public TypeValue(Class<?> maybeType, Object value) {
        this(maybeType, value, TypeOperatorFactory.translator(maybeType));
    }

    public TypeValue(Class<?> maybeType, Object value, TypeOperator typeOperator) {
        this.maybeType = maybeType;
        this.value = value;
        this.maybeTypeOperator = typeOperator;
    }

    /**
     * 以0->0对应关系对应
     *
     * @param types  Class类型
     * @param values 具体值
     * @return 多数据源
     */
    public static Flux<TypeValue> of(Class<?>[] types, Object[] values) {
        if (values == null || types.length != values.length) {
            return Flux.empty();
        }
        return Flux.zip(
                Flux.fromArray(types),
                Flux.fromIterable(Lists.newArrayList(values)),
                (t, v) -> new TypeValue(t, v, TypeOperatorFactory.translator(t)));
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
    public Object tryTransfer() {
        Object maybeActual = value;
        try {
            maybeType.cast(maybeActual);
        } catch (ClassCastException e) {
            if (log.isDebugEnabled()) {
                log.debug("Try cast current value {} for type {} failed", value, maybeType.getName());
            }
            if (Types.isArray(maybeType)) {
                return arrayTransfer();
            } else if (Types.isSet(maybeType) || Types.isList(maybeType)) {
                return collectionTransfer();
            } else if (Types.isMap(maybeType)) {
                return mapTransfer();
            } else {
                try {
                    maybeActual = maybeTypeOperator.convert(maybeActual, maybeType);
                } catch (NullPointerException | NumberFormatException e2) {
                    log.debug("user translator {} get actual value {} type failed", maybeTypeOperator.getClass().getName(), value);
                    return value;
                }
            }
        }
        return maybeActual;
    }

    /**
     * 数组数据转换
     *
     * @return array
     */
    private Object[] arrayTransfer() {
        Class<?> arrayType = ClassUtils.getArrayClassType(maybeType);
        if (arrayType != null) {
            // 1.如果值是String类型，则尝试以json，','分隔赋值
            if (Types.isString(value.getClass())) {
                try {

                    List<String> arrayValues = JsonUtils.readList(value.toString(), String.class);
                    Object[] transferValues = new Object[arrayValues.size()];
                    for (int i = 0; i < arrayValues.size(); i++) {
                        transferValues[i] = new TypeValue(arrayType, arrayValues.get(i)).tryTransfer();
                    }
                    return transferValues;
                } catch (Throwable ex) {
                    // 尝试以','分隔
                    String[] arrayValues = value.toString().split(StringPool.COMMA);
                    Object[] transferValues = new Object[arrayValues.length];
                    for (int i = 0; i < arrayValues.length; i++) {
                        transferValues[i] = new TypeValue(arrayType, arrayValues[i]).tryTransfer();
                    }
                    return transferValues;
                }
            }
            // 2.如果值array类型，则直接进行转换
            if (Types.isArray(value.getClass())) {
                Object[] arrayValues = (Object[]) value;
                Object[] transferValues = new Object[arrayValues.length];
                for (int i = 0; i < arrayValues.length; i++) {
                    transferValues[i] = new TypeValue(arrayType, arrayValues[i]).tryTransfer();
                }
                return transferValues;
            }
            // 3.如果值是集合类型，进行转换
            if (Types.isList(value.getClass()) || Types.isSet(value.getClass())) {
                Collection<Object> arrayValues = (Collection<Object>) value;
                Object[] transferValues = new Object[arrayValues.size()];
                Iterator<Object> iterator = arrayValues.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    transferValues[i] = new TypeValue(arrayType, iterator.next()).tryTransfer();
                }
                return transferValues;
            }
        }
        return new Object[0];
    }


    /**
     * 集合数据转换
     *
     * @return collection
     */
    private Collection<?> collectionTransfer() {
        Class<?> gType;
        try {
            // 获取集合第一个范型类型
            gType = ClassUtils.getSingleActualGenericType(maybeType);
        } catch (ClassNotFoundException e) {
            gType = Object.class;
        }
        Collection<Object> arrayValues = (Collection<Object>) value;
        Collection<Object> collectionValues = null;
        Iterator<Object> iterator = arrayValues.iterator();
        if (Types.isList(maybeType)) {
            collectionValues = Lists.newArrayListWithCapacity(arrayValues.size());
            while (iterator.hasNext()) {
                Object next = iterator.next();
                collectionValues.add(new TypeValue(gType, next).tryTransfer());
            }
        }
        if (Types.isSet(maybeType)) {
            collectionValues = Sets.newHashSetWithExpectedSize(arrayValues.size());
            while (iterator.hasNext()) {
                Object next = iterator.next();
                collectionValues.add(new TypeValue(gType, next).tryTransfer());
            }
        }
        return collectionValues;
    }

    /**
     * map数据转换
     *
     * @return the map
     */
    private Map<?, ?> mapTransfer() {
        Class<?> keyType = Object.class;
        Class<?> valueType = Object.class;
        try {
            Class<?>[] multiActualGenericType = ClassUtils.getMultiActualGenericType(maybeType);
            if (multiActualGenericType.length > 1) {
                keyType = multiActualGenericType[0];
                valueType = multiActualGenericType[1];
            }
        } catch (ClassNotFoundException e) {
            // ignore
        }
        Map<Object, Object> mapValues = Maps.newHashMap();
        // 1.判断是否为string数据
        if (Types.isString(value.getClass())) {
            try {
                Map<Object, Object> jsonValues = JsonUtils.readMap(value.toString(), Object.class, Object.class);
                for (Map.Entry<Object, Object> jsonEntry : jsonValues.entrySet()) {
                    Object jKey = jsonEntry.getKey();
                    Object jValue = jsonEntry.getValue();
                    Object tKey = new TypeValue(keyType, jKey).tryTransfer();
                    Object tValue = new TypeValue(valueType, jValue).tryTransfer();
                    mapValues.put(tKey, tValue);
                }
                return mapValues;
            } catch (Throwable ex) {
                // ignore
            }
        }
        // 2.判断是否为map
        if (Types.isMap(value.getClass())) {
            Map<Object, Object> oValues = (Map<Object, Object>) value;
            for (Map.Entry<Object, Object> oEntry : oValues.entrySet()) {
                Object jKey = oEntry.getKey();
                Object jValue = oEntry.getValue();
                Object tKey = new TypeValue(keyType, jKey).tryTransfer();
                Object tValue = new TypeValue(valueType, jValue).tryTransfer();
                mapValues.put(tKey, tValue);
            }
            return mapValues;
        }
        return mapValues;
    }
}
