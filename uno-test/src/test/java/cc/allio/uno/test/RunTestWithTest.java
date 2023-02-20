package cc.allio.uno.test;

import cc.allio.uno.test.env.DatasourceTestEnvironment;
import cc.allio.uno.test.env.MybatisPlusTestEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironmentFacade;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;

@RunTest(envs = {
        @RunTest.Environment(env = DatasourceTestEnvironment.class),
        @RunTest.Environment(env = MybatisPlusTestEnvironment.class)},
        components = {RunTestWithTest.TestBean.class},
        profile = "whatever",
        active = "")
class RunTestWithTest extends BaseCoreTest {

    @Inject
    private TestBean testBean;

    @Test
    void testEnvCount() {
        TestSpringEnvironmentFacade env = getEnv();
        assertEquals(2, env.size());
    }

    @Test
    void testConfigFileReader() {
        String property = getProperty("automic.uno");
        assertEquals("test", property);
    }

    @Test
    void testGetSqlSessionTemplate() {
        assertDoesNotThrow(() -> {
            SqlSessionTemplate sessionTemplate = getBean(SqlSessionTemplate.class);
            Configuration configuration = sessionTemplate.getConfiguration();
            System.out.println(configuration);
        });
    }

    @org.springframework.context.annotation.Configuration
    static class TestBean {

    }

}
