package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，三元消费者
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 * @author jiangwei
 * @date 2024/1/26 18:36
 * @since 1.1.7
 */
@FunctionalInterface
public interface MethodTerConsumer<T, K, U> extends Serializable, LambdaMethod {

    void accept(T t, K k, U u);
}
