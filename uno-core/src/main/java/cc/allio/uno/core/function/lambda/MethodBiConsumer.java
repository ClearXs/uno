package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::getName
 *
 * @author jiangwei
 * @date 2024/1/26 18:34
 * @see java.util.function.BiConsumer
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodBiConsumer<T, K> extends Serializable, LambdaMethod {

    void accept(T t, K k);
}
