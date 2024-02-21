package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for function apply method
 *
 * @author jiangwei
 * @date 2024/2/18 15:56
 * @see MethodFunction
 * @since 1.1.6
 */
public interface ThrowingMethodFunction<T, K> extends Serializable, LambdaMethod {

    K apply(T t) throws Throwable;
}
