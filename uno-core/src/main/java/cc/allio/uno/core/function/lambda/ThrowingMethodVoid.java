package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for void method
 *
 * @author jiangwei
 * @date 2024/2/18 16:03
 * @see MethodVoid
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodVoid extends Serializable, LambdaMethod {

    void accept() throws Throwable;
}
