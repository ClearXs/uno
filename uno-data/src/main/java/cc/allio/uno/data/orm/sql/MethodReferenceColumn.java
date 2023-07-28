package cc.allio.uno.data.orm.sql;

import cc.allio.uno.core.function.SerializedLambda;
import cc.allio.uno.core.function.StaticMethodReference;

/**
 * 可以通过静态方法引用来获取实体（<b>需要实例实现{@link java.io.Serializable}</b>）的字段名称：
 * <pre>
 *     select(User::getName).eq(User::getAge, 2)...
 * </pre>
 *
 * @author jiangwei
 * @date 2023/1/5 15:36
 * @since 1.1.4
 */
public interface MethodReferenceColumn<T> extends StaticMethodReference<T> {

    /**
     * 获取Column名称
     *
     * @return Column名称
     * @throws java.io.NotSerializableException 当实体没有实现{@link java.io.Serializable}时抛出
     */
    default String getColumn() {
        return SerializedLambda.of(this).getFieldName();
    }
}
