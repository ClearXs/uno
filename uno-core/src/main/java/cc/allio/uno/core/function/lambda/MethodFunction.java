package cc.allio.uno.core.function.lambda;

import cc.allio.uno.core.reflect.ReflectTools;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::getName
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 * @author j.x
 * @since 1.1.7
 */
@FunctionalInterface
public interface MethodFunction<T, R> extends Serializable, LambdaMethod {

    R apply(T t);

    /**
     * 获取参数值的类型
     */
    default Class<T> getParameterType() {
        return (Class<T>) ReflectTools.getGenericType(this, MethodFunction.class, 0);
    }

    /**
     * 获取返回值的类型
     */
    default Class<R> getReturnType() {
        return (Class<R>) ReflectTools.getGenericType(this, MethodFunction.class, 1);
    }
}
