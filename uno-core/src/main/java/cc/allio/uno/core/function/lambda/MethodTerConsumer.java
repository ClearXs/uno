package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，三元消费者
 *
 * @author jiangwei
 * @date 2024/1/26 18:36
 * @since 1.1.6
 */
@FunctionalInterface
public interface MethodTerConsumer<T, K, U> extends Serializable, LambdaMethod {

    void accept(T t, K k, U u);
}
