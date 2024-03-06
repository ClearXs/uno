package cc.allio.uno.test.annotation;

import cc.allio.uno.test.Inject;
import cc.allio.uno.test.annotation.mapper.TestMapper;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RunTest
@MybatisPlusEnv
public class MybatisEnvTest {

    @Autowired
    @Resource
    @Inject
    private TestMapper testMapper;

    @Test
    void testGetBean() {
        Assertions.assertNotNull(testMapper);
    }
}
