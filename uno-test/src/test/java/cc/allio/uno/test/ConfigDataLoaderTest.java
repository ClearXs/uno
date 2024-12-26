package cc.allio.uno.test;

import org.junit.jupiter.api.Test;

/**
 * 测试从加载类路径下的配置文件
 *
 * @author j.x
 * @since 1.0
 */
public class ConfigDataLoaderTest extends CoreTest {

    @Test
    void testLoad() {
        assertEquals("uno", getProperty("allio"));
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }
}
