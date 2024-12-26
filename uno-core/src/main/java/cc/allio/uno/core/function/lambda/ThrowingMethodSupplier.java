package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for supplier get method
 *
 * @author j.x
 * @see MethodSupplier
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodSupplier<T> extends Serializable, LambdaMethod {

    /**
     * get method, potentially throwing an exception.
     *
     * @return result
     */
    T get() throws Throwable;
}
