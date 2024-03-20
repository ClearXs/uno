package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.runner.Runner;
import org.springframework.core.Ordered;

/**
 * 拓展{@link Environment}的功能。 {@link RunTest#visitor()}指定的class上进行实例化 or 系统内默认的。与{@link Runner}逻辑一致。获取优先级
 *
 * @author j.x
 * @date 2023/3/3 12:18
 * @since 1.1.4
 */
public interface Visitor extends Ordered {

    /**
     * 实现类进行实现，拓展环境功能
     *
     * @param coreTest coreTest
     * @param env      env
     * @throws Throwable
     */
    void visit(CoreTest coreTest, Environment env) throws Throwable;

    @Override
    default int getOrder() {
        return 0;
    }
}
