package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for bi consumer accept method
 *
 * @author jiangwei
 * @date 2024/2/18 15:46
 * @see MethodBiConsumer
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodBiConsumer<T, K> extends Serializable, LambdaMethod {

    void accept(T t, K k) throws Throwable;
}
