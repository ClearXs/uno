package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author j.x
 * @see MethodBiPredicate
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodBiPredicate<T, U> extends Serializable, LambdaMethod {

    /**
     * test predicate, potentially throwing an exception.
     *
     * @param t first argument
     * @param u second argument
     * @return predicate result
     */
    boolean test(T t, U u) throws Throwable;
}
