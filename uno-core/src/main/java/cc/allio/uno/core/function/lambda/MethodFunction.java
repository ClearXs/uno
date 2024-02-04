package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.util.ReflectTool;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::getName
 *
 * @author jiangwei
 * @date 2024/1/26 18:31
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodFunction<T, K> extends Serializable, LambdaMethod {

    K apply(T t);

    /**
     * 获取参数值的类型
     */
    default Class<T> getParameterType() {
        return (Class<T>) ReflectTool.getGenericType(this, MethodFunction.class, 0);
    }

    /**
     * 获取返回值的类型
     */
    default Class<K> getReturnType() {
        return (Class<K>) ReflectTool.getGenericType(this, MethodFunction.class, 1);
    }
}
