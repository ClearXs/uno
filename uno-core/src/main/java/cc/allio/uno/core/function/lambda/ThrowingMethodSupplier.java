package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for supplier get method
 *
 * @author jiangwei
 * @date 2024/2/18 15:59
 * @see MethodSupplier
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodSupplier<T> extends Serializable, LambdaMethod {

    T get() throws Throwable;
}
