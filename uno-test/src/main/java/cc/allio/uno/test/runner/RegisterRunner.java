package cc.allio.uno.test.runner;

import cc.allio.uno.test.CoreTest;
import org.springframework.context.ApplicationContext;

/**
 * <b>标识接口</b>spring进行Component注册时进行执行
 *
 * @author j.x
 * @date 2022/10/29 10:07
 * @since 1.1.0
 */
public interface RegisterRunner extends Runner {

    @Override
    default void run(CoreTest coreTest) throws Throwable {
        onRegister(coreTest);
    }

    /**
     * 当{@link ApplicationContext}实例后调用
     *
     * @throws Throwable 调用过程中出现异常抛出
     */
    void onRegister(CoreTest coreTest) throws Throwable;
}
