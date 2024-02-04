package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现
 *
 * @author jiangwei
 * @date 2024/1/26 18:39
 * @see java.util.function.BiFunction
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodBiFunction<T, U, R> extends Serializable, LambdaMethod {

    R apply(T t, U u);
}
