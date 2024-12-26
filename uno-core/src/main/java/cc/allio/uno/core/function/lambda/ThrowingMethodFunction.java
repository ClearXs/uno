package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for function apply method
 *
 * @author j.x
 * @see MethodFunction
 * @since 1.1.7
 */
public interface ThrowingMethodFunction<T, K> extends Serializable, LambdaMethod {

    /**
     * apply, potentially throwing an exception.
     *
     * @param t the input
     * @return result
     */
    K apply(T t) throws Throwable;
}
