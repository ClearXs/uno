package cc.allio.uno.test.runner;

import cc.allio.uno.test.BaseCoreTest;
import lombok.Data;

import java.util.Collection;

/**
 * 基于注解构建注册执行，满足执行环境的上下文，如构建某些组件
 *
 * @author jiangwei
 * @date 2022/10/28 16:24
 * @since 1.1.0
 */
@FunctionalInterface
public interface Runner {

    /**
     * 子类实现，针对某一种具体的注解进行实现
     *
     * @param coreTest 核心测试对象
     * @throws Throwable 执行过程中出现错误抛出
     */
    void run(BaseCoreTest coreTest) throws Throwable;

    /**
     * 是否是共享的Runner
     *
     * @return 默认为true
     */
    default boolean shared() {
        return true;
    }

    @Data
    class CoreRunner {

        private final Collection<Runner> registerRunner;
        private final Collection<Runner> refreshCompleteRunner;

        private final Collection<Runner> closeRunner;

        public CoreRunner(Collection<Runner> registerRunner,
                          Collection<Runner> refreshCompleteRunner,
                          Collection<Runner> closeRunner) {
            this.registerRunner = registerRunner;
            this.refreshCompleteRunner = refreshCompleteRunner;
            this.closeRunner = closeRunner;
        }

    }
}
