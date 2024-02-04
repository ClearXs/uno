package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.util.ReflectTool;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::setName
 *
 * @author jiangwei
 * @date 2024/1/26 18:29
 * @see java.util.function.Supplier
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodSupplier<T> extends Serializable, LambdaMethod {

    T get();

    /**
     * 获取类型
     */
    default Class<T> getType() {
        return (Class<T>) ReflectTool.getGenericType(this, MethodSupplier.class);
    }
}
