package cc.allio.uno.test.env;

import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.BaseCoreTest;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;

@RunTest(envs = {
        @RunTest.Environment(env = MybatisPlusTestEnvironment.class, classArgs = MybatisPlusTestEnvironmentTest.TestMapper.class)})
public class MybatisPlusTestEnvironmentTest extends BaseCoreTest {

    @Test
    void testGetSqlSessionTemplate() {
        SqlSessionTemplate sqlSessionTemplate = getBean(SqlSessionTemplate.class);

        assertNotNull(sqlSessionTemplate);
    }


    @MapperScan
    static class TestMapper {

    }
}
