package cc.allio.uno.test;

import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.Environment;
import cc.allio.uno.test.env.EnvironmentFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 数据源环境测试
 *
 * @author jiangwei
 * @date 2022/8/29 11:30
 * @since 1.0.9
 */
public class DataSourceEnvironmentTest extends CoreTest {


    @Override
    protected void onEnvBuild() {

    }

    @Override
    public Environment supportEnv() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceProperties.setUrl("jdbc:mysql://192.168.2.29:3306/migration?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
        dataSourceProperties.setUsername("root");
        dataSourceProperties.setPassword("123456");
        return new EnvironmentFacade(new DataSourceEnvironment());
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    @Test
    void testDatasource() {
        Map<String, DataSource> beansOfType = getContext().getBeansOfType(DataSource.class);
        assertEquals(beansOfType.size(), 1);
    }
}