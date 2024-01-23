package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.config.DbAutoConfiguration;
import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.Parameter;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.DynamicDataSourceEnv;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import org.junit.jupiter.api.Test;

@RunTest(active = "datasource", components = {DbAutoConfiguration.class, ExecutorInitializerAutoConfiguration.class})
@MybatisEnv
@DynamicDataSourceEnv
public class DynamicDataSourceTest extends BaseTestCase {

    @Test
    void testGetDatasource(@Parameter CoreTest coreTest) {

        System.out.println(coreTest);
    }
}
