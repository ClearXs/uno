package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.reflect.ReflectTools;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::setName
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 * @author j.x
 * @date 2024/1/26 18:29
 * @see java.util.function.Supplier
 * @since 1.1.7
 */
@FunctionalInterface
public interface MethodSupplier<T> extends Serializable, LambdaMethod {

    T get();

    /**
     * 获取类型
     */
    default Class<T> getType() {
        return (Class<T>) ReflectTools.getGenericType(this, MethodSupplier.class);
    }
}
