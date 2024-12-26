package cc.allio.uno.core.function.lambda;

import java.io.Serializable;

/**
 * 基于函数式接口的Lambda实现，如this::getName
 * <p>该接口包含能够获取Lambda方法名、字段名、Lambda方法的序列化信息等。</p>
 * @author j.x
 * @see java.util.function.Consumer
 * @since 1.1.7
 */
@FunctionalInterface
public interface MethodConsumer<T> extends Serializable, LambdaMethod {

    void accept(T t);
}
