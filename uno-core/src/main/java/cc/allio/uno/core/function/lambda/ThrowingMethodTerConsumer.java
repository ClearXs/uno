package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter consumer accept method
 *
 * @author jiangwei
 * @date 2024/2/18 16:00
 * @see MethodTerConsumer
 * @since 1.1.6
 */
public interface ThrowingMethodTerConsumer<T, K, U> extends Serializable, LambdaMethod {

    void accept(T t, K k, U u) throws Throwable;
}
