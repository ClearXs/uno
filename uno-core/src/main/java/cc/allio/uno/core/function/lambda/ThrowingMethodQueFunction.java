package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for que function apply method
 *
 * @author jiangwei
 * @date 2024/2/18 15:58
 * @see MethodQueFunction
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodQueFunction<T1, T2, T3, T4, R> extends Serializable, LambdaMethod {

    R apply(T1 t1, T2 t2, T3 t3, T4 t4) throws Throwable;
}
