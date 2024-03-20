package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for ter predicate test method
 *
 * @author j.x
 * @date 2024/2/18 16:02
 * @see MethodTerPredicate
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodTerPredicate<T, U, P> extends Serializable, LambdaMethod {

    /**
     * test, potentially throwing an exception.
     *
     * @param t the first argument
     * @param u the second argument
     * @param p the third argument
     * @return true if the test object is valid
     */
    boolean test(T t, U u, P p) throws Throwable;
}
