package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，三元函数
 *
 * @author jiangwei
 * @date 2024/1/26 18:40
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodTerFunction<T, U, L, R> extends Serializable, LambdaMethod {

    R apply(T t, U u, L l);
}
