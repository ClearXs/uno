package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author j.x
 * @date 2024/2/18 15:57
 * @see MethodPredicate
 * @since 1.1.7
 */
@FunctionalInterface
public interface ThrowingMethodPredicate<T> extends Serializable, LambdaMethod {

    /**
     * test, potentially throwing an exception
     *
     * @param t the test object
     * @return true if the test object is valid
     */
    boolean test(T t) throws Throwable;
}
