package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter function apply method
 *
 * @author jiangwei
 * @date 2024/2/18 16:01
 * @see MethodTerFunction
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodTerFunction<T, U, L, R> extends Serializable, LambdaMethod {

    /**
     * apply method, potentially throwing an exception.
     *
     * @param t the first argument
     * @param u the second argument
     * @param l the third argument
     * @return result
     */
    R apply(T t, U u, L l) throws Throwable;
}
