package cc.allio.uno.test.runner;

import cc.allio.uno.test.CoreTest;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <b>标识接口</b>当spring关闭时执行
 *
 * @author j.x
 * @since 1.1.0
 */
public interface CloseRunner extends Runner {

    @Override
    default void run(CoreTest coreTest) throws Throwable {
        onClose(coreTest);
    }

    /**
     * 当{@link ConfigurableApplicationContext#close()}调用后执行
     *
     * @param coreTest coreTest
     * @throws Throwable 调用过程中出现异常抛出
     */
    void onClose(CoreTest coreTest) throws Throwable;
}
