package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author jiangwei
 * @date 2024/2/18 15:53
 * @see MethodBiPredicate
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodBiPredicate<T, U> extends Serializable, LambdaMethod {

    boolean test(T t, U u) throws Throwable;
}
