package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter consumer accept method
 *
 * @author j.x
 * @date 2024/2/18 16:00
 * @see MethodTerConsumer
 * @since 1.1.7
 */
public interface ThrowingMethodTerConsumer<T, K, U> extends Serializable, LambdaMethod {

    /**
     * accept, potentially throwing an exception.
     *
     * @param t the first argument
     * @param k the second argument
     * @param u the third argument
     */
    void accept(T t, K k, U u) throws Throwable;
}
