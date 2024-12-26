package cc.allio.uno.core.util.template;

import cc.allio.uno.core.function.lambda.MethodFunction;

/**
 * get specific type {@link T} translate type {@link R}
 *
 * @param <T> type T
 * @param <R> translate R
 * @author j.x
 * @since 1.1.9
 */
public interface VariableResolve<T, R> extends MethodFunction<T, R> {
}
