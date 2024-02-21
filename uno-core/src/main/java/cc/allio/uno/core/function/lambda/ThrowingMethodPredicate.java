package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author jiangwei
 * @date 2024/2/18 15:57
 * @see MethodPredicate
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodPredicate<T> extends Serializable, LambdaMethod {

    boolean test(T t) throws Throwable;
}
