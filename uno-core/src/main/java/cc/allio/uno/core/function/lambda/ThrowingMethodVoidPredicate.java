package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * exception for predicate test method
 *
 * @author jiangwei
 * @date 2024/2/18 16:04
 * @see MethodVoidPredicate
 * @since 1.1.6
 */
@FunctionalInterface
public interface ThrowingMethodVoidPredicate extends Serializable, LambdaMethod {

    boolean test() throws Throwable;
}
