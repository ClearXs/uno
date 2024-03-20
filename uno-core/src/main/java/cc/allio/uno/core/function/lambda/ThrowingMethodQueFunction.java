package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for que function apply method
 *
 * @author j.x
 * @date 2024/2/18 15:58
 * @see MethodQueFunction
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodQueFunction<T1, T2, T3, T4, R> extends Serializable, LambdaMethod {

    /**
     * apply method, potentially throwing an exception.
     *
     * @param t1 first argument
     * @param t2 second argument
     * @param t3 third argument
     * @param t4 fourth argument
     * @return result
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
