package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter predicate test method
 *
 * @author jiangwei
 * @date 2024/2/18 16:02
 * @see MethodTerPredicate
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodTerPredicate<T, U, P> extends Serializable, LambdaMethod {

    boolean test(T t, U u, P p) throws Throwable;
}
