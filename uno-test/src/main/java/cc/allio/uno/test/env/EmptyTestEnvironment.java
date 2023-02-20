package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;

/**
 * 空Test Environment
 *
 * @author jiangwei
 * @date 2022/12/30 14:44
 * @since 1.1.4
 */
public class EmptyTestEnvironment implements TestSpringEnvironment {
    @Override
    public void support(BaseCoreTest test) {

    }
}
