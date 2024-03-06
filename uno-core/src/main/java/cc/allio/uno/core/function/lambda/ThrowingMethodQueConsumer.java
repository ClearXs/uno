package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for que consumer accept method
 *
 * @author jiangwei
 * @date 2024/2/18 15:57
 * @see MethodQueConsumer
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodQueConsumer<T, K, U, L> extends Serializable, LambdaMethod {

    /**
     * accept, potentially throwing an exception.
     *
     * @param t the first argument
     * @param k the second argument
     * @param u the third argument
     * @param l the fourth argument
     */
    void accept(T t, K k, U u, L l) throws Throwable;
}
