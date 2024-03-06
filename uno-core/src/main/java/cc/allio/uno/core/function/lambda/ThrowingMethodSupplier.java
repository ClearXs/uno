package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for supplier get method
 *
 * @author jiangwei
 * @date 2024/2/18 15:59
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
