package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for consumer accept method
 *
 * @author j.x
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
