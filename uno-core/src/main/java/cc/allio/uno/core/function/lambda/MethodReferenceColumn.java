package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.StringPool;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * 可以通过静态方法引用来获取实体（<b>需要实例实现{@link java.io.Serializable}</b>）的字段名称：
 * <pre>
 *     select(User::getName).eq(User::getAge, 2)...
 * </pre>
 *
 * @author j.x
 * @date 2023/1/5 15:36
 * @since 1.1.4
 */
public interface MethodReferenceColumn<T> extends StaticMethodReference<T> {

    /**
     * 获取实体类型
     *
     * @return class or empty
     */
    default Class<T> getEntityType() {
        SerializedLambda serializedLambda = SerializedLambda.of(this);
        String implClass = serializedLambda.getImplClass();
        try {
            return (Class<T>) Class.forName(implClass.replace(StringPool.SLASH, StringPool.ORIGIN_DOT));
        } catch (ClassNotFoundException e) {
            // ignore
            return null;
        }
    }

    /**
     * 获取Column名称
     *
     * @return Column名称
     * @throws java.io.NotSerializableException 当实体没有实现{@link java.io.Serializable}时抛出
     */
    default String getColumn() {
        return SerializedLambda.of(this).getFieldName();
    }

    /**
     * 获取实体的{@link Field}实例
     *
     * @return field 实例 or empty
     */
    default Field getField() {
        Class<T> entityType = getEntityType();
        if (entityType != null) {
            return FieldUtils.getField(entityType, getColumn(), true);
        } else {
            return null;
        }
    }
}
