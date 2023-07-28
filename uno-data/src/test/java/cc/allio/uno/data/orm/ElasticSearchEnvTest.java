package cc.allio.uno.data.orm;

import cc.allio.uno.data.orm.config.ElasticSearchAutoConfiguration;
import cc.allio.uno.data.orm.executor.elasticsearch.EsSQLCommandExecutor;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.EsEnv;
import cc.allio.uno.test.env.annotation.properties.EsProperties;
import org.junit.jupiter.api.Test;

@RunTest(components = ElasticSearchAutoConfiguration.class)
@EsEnv
@EsProperties(uris = "http://43.143.195.208:9200")
public class ElasticSearchEnvTest extends BaseTestCase {

    @Inject
    private EsSQLCommandExecutor sqlCommandExecutor;

    @Test
    void testNotNull() {
        assertNotNull(sqlCommandExecutor);
    }
}
