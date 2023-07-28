package cc.allio.uno.test.runner;

import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import org.junit.jupiter.api.Test;

public class AnnoMetadataRunnerTest extends BaseTestCase {

    @Test
    void testMetadata() throws Throwable {
        CoreTest testCore = new TestCore();
        AnnoMetadataRunner runner = new AnnoMetadataRunner();
        runner.run(testCore);
    }


    @MybatisEnv
    static class TestCore extends CoreTest {

    }
}
