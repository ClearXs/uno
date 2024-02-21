package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for consumer accept method
 *
 * @author jiangwei
 * @date 2024/2/18 15:54
 * @see MethodConsumer
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodConsumer<T> extends Serializable, LambdaMethod {

    void accept(T t) throws Throwable;
}
