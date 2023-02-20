package cc.allio.uno.test.env;

import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.BaseCoreTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

@RunTest(envs = @RunTest.Environment(env = DatasourceTestEnvironment.class), active = "")
public class DatasourceTestEnvironmentTest extends BaseCoreTest {
    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    @Test
    void testDataSourceProperties() {
        assertDoesNotThrow(() -> {
            DataSourceProperties properties = getBean(DataSourceProperties.class);
            assertEquals("jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true", properties.getUrl());
            assertEquals("root", properties.getUsername());
            assertEquals("123456", properties.getPassword());
            assertEquals("com.mysql.cj.jdbc.Driver", properties.getDriverClassName());
        });
    }

    @Test
    void testDataSource() {
        assertDoesNotThrow(() -> {
            getBean(DataSource.class);
        });
    }
}
