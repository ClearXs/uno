package cc.allio.uno.test;

import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.MybatisPlusEnvironment;
import cc.allio.uno.test.env.EnvironmentFacade;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;

@RunTest(
        components = {RunTestWithTest.TestBean.class},
        profile = "whatever",
        active = "")
class RunTestWithTest extends CoreTest {

    @Inject
    private TestBean testBean;

    @Test
    void testEnvCount() {
        EnvironmentFacade env = getEnv();
        assertEquals(2, env.size());
    }

    @Test
    void testConfigFileReader() {
        String property = getProperty("allio.uno");
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
