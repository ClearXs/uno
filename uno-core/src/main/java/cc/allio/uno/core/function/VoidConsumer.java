package cc.allio.uno.core.function;

import java.util.function.Consumer;

/**
 * 不接收如何参数的消费者
 *
 * @author j.x
 * @date 2024/1/9 15:57
 * @since 1.1.7
 */
@FunctionalInterface
public interface VoidConsumer extends Consumer<Object>, Action<Object> {

    @Override
    default void accept(Object o) {
        doAccept();
    }

    void doAccept();
}
