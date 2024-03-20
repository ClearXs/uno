package cc.allio.uno.core.function;

/**
 * 拓展Consumer函数式接口，用于接收三元参数
 *
 * @author j.x
 * @date 2024/1/9 15:51
 * @since 1.1.7
 */
@FunctionalInterface
public interface TernaryConsumer<T, Q, P> {
    void accept(T t, Q q, P p);
}
