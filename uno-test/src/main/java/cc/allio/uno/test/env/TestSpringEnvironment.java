package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;

/**
 * 基础的Spirng测试环境
 *
 * @author jiangwei
 * @date 2022/2/14 14:05
 * @since 1.0
 */
public interface TestSpringEnvironment extends TestEnvironment {

    /**
     * 环境的支持
     *
     * @param test 测试基础类
     */
    void support(BaseCoreTest test);
}
