package cc.allio.uno.core.api;

import cc.allio.uno.core.util.ObjectUtils;

import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * 执行步骤。步骤将会按照{@link #then(Supplier)}一步一步执行下去，直到获取的值不为null或者走完步骤。
 *
 * @author j.x
 * @date 2024/2/6 23:38
 * @since 1.1.7
 */
public interface Step<T> {

    /**
     * 开始并获取{@link Step}实例
     *
     * @param <T> 数据类型
     * @return Step
     */
    static <T> Step<T> start() {
        return new StepImpl<>();
    }

    /**
     * 步骤
     *
     * @param provider provider
     * @return Step
     */
    Step<T> then(Supplier<T> provider);

    /**
     * 终止
     *
     * @return 获取的结果
     */
    T end();

    class StepImpl<T> implements Step<T>, Self<StepImpl<T>> {

        LinkedList<Supplier<T>> providers = new LinkedList<>();

        @Override
        public Step<T> then(Supplier<T> provider) {
            this.providers.add(provider);
            return self();
        }

        @Override
        public T end() {
            Supplier<T> provider;
            while ((provider = providers.pollFirst()) != null) {
                T value = provider.get();
                if (ObjectUtils.isNotEmpty(value)) {
                    return value;
                }
            }
            return null;
        }
    }

}
