package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for bi function apply method
 *
 * @author jiangwei
 * @date 2024/2/18 15:49
 * @see MethodBiFunction
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodBiFunction<T, U, R> extends Serializable, LambdaMethod {

    /**
     * apply method, potentially throwing an exception.
     *
     * @param t first argument
     * @param u second argument
     * @return result
     */
    R apply(T t, U u) throws Throwable;
}
