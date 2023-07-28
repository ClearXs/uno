package cc.allio.uno.test.runner;

import cc.allio.uno.test.CoreTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * 基于注解构建注册执行，基于spring环境，执行环境的上下文，如构建某些组件。
 * <p>如：</p>
 * <ul>
 *     <li>{@link RegisterRunner}：当spring context实例化后进行调用</li>
 *     <li>{@link RegisterRunner}：当{@link ConfigurableApplicationContext#refresh()}调用后进行调用</li>
 *     <li>{@link CloseRunner}：当{@link ConfigurableApplicationContext#close()}调用后进行调用</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2022/10/28 16:24
 * @since 1.1.0
 */
@FunctionalInterface
public interface Runner extends Ordered {

    /**
     * 子类实现，针对某一种具体的注解进行实现
     *
     * @param coreTest 核心测试对象
     * @throws Throwable 执行过程中出现错误抛出
     */
    void run(CoreTest coreTest) throws Throwable;

    /**
     * 是否是共享的Runner
     *
     * @return 默认为true
     */
    default boolean shared() {
        return true;
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
