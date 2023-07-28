package cc.allio.uno.test.runner;

import cc.allio.uno.test.CoreTest;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <b>标识接口</b>当spring上下文刷新完成后执行
 *
 * @author jiangwei
 * @date 2022/10/29 10:05
 * @since 1.1.0
 */
public interface RefreshCompleteRunner extends Runner {

    @Override
    default void run(CoreTest coreTest) throws Throwable {
        onRefreshComplete(coreTest);
    }

    /**
     * 当{@link ConfigurableApplicationContext#refresh()}调用
     *
     * @param coreTest coreTest
     * @throws Throwable 调用过程中出现异常抛出
     */
    void onRefreshComplete(CoreTest coreTest) throws Throwable;
}
