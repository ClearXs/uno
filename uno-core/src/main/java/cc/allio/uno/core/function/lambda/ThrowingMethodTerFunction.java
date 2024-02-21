package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter function apply method
 *
 * @author jiangwei
 * @date 2024/2/18 16:01
 * @see MethodTerFunction
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodTerFunction<T, U, L, R> extends Serializable, LambdaMethod {

    R apply(T t, U u, L l) throws Throwable;
}
