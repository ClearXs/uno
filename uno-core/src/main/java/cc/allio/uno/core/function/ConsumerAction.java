package cc.allio.uno.core.function;

import java.util.function.Consumer;

/**
 * consumer action
 *
 * @author j.x
 * @date 2024/1/9 16:00
 * @since 1.1.7
 */
public interface ConsumerAction<T> extends Consumer<T>, Action<T> {
}
