package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for bi consumer accept method
 *
 * @author j.x
 * @date 2024/2/18 15:46
 * @see MethodBiConsumer
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodBiConsumer<T, K> extends Serializable, LambdaMethod {

    /**
     * accept method, potentially throwing an exception.
     *
     * @param t the first argument
     * @param k the second argument
     */
    void accept(T t, K k) throws Throwable;
}
