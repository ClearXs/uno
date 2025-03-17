package cc.allio.uno.core.reflect;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * instantiation strategy
 *
 * @author j.x
 * @since 1.1.7
 */
public interface InstantiationFeature<I> {

    /**
     * 执行对应特性策略
     *
     * @param instantiation 实例化参数
     */
    void execute(Instantiation<I> instantiation);

    /**
     * of a sort feature
     *
     * @param <I> instance type
     * @return sort instantiation feature
     */
    static <I> InstantiationFeature<I> sort() {
        return new SortFeature<>();
    }

    /**
     * of a callback feature
     *
     * @param <I> instance type
     * @return callback instantiation feature
     */
    static <I> InstantiationFeature<I> callback(Consumer<Instantiation<I>> consumer) {
        return new CallbackFeature<>(consumer);
    }

    /**
     * of a deduplicate feature
     *
     * @param <I> instance type
     * @return deduplicate instantiation feature
     */
    static <I> InstantiationFeature<I> deduplicate() {
        return new DeDuplicationFeature<>();
    }

    /**
     * 排序策略
     */
    class SortFeature<I> implements InstantiationFeature<I> {

        @Override
        public void execute(Instantiation<I> instantiation) {
            Class<? extends I>[] waitForInstanceClasses = instantiation.getWaitForInstanceClasses();
            AnnotationAwareOrderComparator.sort(waitForInstanceClasses);
            instantiation.rewriteInstanceClasses(waitForInstanceClasses);
        }
    }

    /**
     * 回调策略
     */
    class CallbackFeature<I> implements InstantiationFeature<I> {

        private final Consumer<Instantiation<I>> consumer;

        public CallbackFeature(Consumer<Instantiation<I>> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void execute(Instantiation<I> instantiation) {
            consumer.accept(instantiation);
        }
    }

    /**
     * 去重策略（简单的stream distinct进行去重）
     */
    class DeDuplicationFeature<I> implements InstantiationFeature<I> {

        @Override
        public void execute(Instantiation<I> instantiation) {
            Class<? extends I>[] waitForInstanceClasses = instantiation.getWaitForInstanceClasses();
            instantiation.rewriteInstanceClasses(Stream.of(waitForInstanceClasses).distinct().toArray(Class[]::new));
        }
    }
}
