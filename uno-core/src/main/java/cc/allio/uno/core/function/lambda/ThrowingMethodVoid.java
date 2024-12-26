package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for void method
 *
 * @author j.x
 * @see MethodVoid
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodVoid extends Serializable, LambdaMethod {

    /**
     * accept, potentially throwing an exception.
     */
    void accept() throws Throwable;
}
