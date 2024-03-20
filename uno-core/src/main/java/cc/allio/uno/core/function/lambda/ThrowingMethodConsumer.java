package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for consumer accept method
 *
 * @author j.x
 * @date 2024/2/18 15:54
 * @see MethodConsumer
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodConsumer<T> extends Serializable, LambdaMethod {

    /**
     * accept, potentially throwing an exception
     *
     * @param t the input
     */
    void accept(T t) throws Throwable;

}
