package cc.allio.uno.test;

import cc.allio.uno.test.env.EnvironmentFacade;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;

@RunMapperTest(mapperScan = RunMapperTestWithTest.TestMapperScan.class)
class RunMapperTestWithTest extends CoreTest {

    @Test
    void testEnvCount() {
        EnvironmentFacade env = getEnv();
        assertEquals(2, env.size());
    }

    @MapperScan("xxx")
    public static class TestMapperScan {

    }
}
