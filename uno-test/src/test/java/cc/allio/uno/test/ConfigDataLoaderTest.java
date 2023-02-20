package cc.allio.uno.test;

import cc.allio.uno.test.env.TestSpringEnvironment;
import org.junit.jupiter.api.Test;

/**
 * 测试从加载类路径下的配置文件
 *
 * @author jiangwei
 * @date 2022/2/26 14:11
 * @see org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
 * @see org.springframework.boot.context.config.ConfigFileApplicationListener
 * @since 1.0
 */
public class ConfigDataLoaderTest extends BaseCoreTest {
    @Override
    protected void onEnvBuild() {

    }

    @Test
    void testLoad() {
        assertEquals("test", getProperty("automic.uno"));
    }

    @Override
    public TestSpringEnvironment supportEnv() {
        return null;
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }
}
