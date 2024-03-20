package cc.allio.uno.test.env;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.test.CoreTest;

import java.util.Set;

/**
 * VisitorEnvironment
 *
 * @author j.x
 * @date 2023/3/3 13:41
 * @since 1.1.4
 */
public abstract class VisitorEnvironment implements Environment {

    @Override
    public void support(CoreTest coreTest) throws Throwable {
        Set<Visitor> visitors = coreTest.getVisitors();
        if (CollectionUtils.isNotEmpty(visitors)) {
            for (Visitor visitor : visitors) {
                visitor.visit(coreTest, this);
            }
        }
        onSupport(coreTest);
    }

    /**
     * 子类实现，提供所需的环境。
     *
     * @param coreTest coreTest
     */
    protected abstract void onSupport(CoreTest coreTest) throws Throwable;
}
