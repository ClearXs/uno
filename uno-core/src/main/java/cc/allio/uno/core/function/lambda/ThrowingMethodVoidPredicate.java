package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author j.x
 * @see MethodVoidPredicate
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodVoidPredicate extends Serializable, LambdaMethod {

    /**
     * test method, potentially throwing an exception.
     *
     * @return true if test pass
     */
    boolean test() throws Throwable;
}
