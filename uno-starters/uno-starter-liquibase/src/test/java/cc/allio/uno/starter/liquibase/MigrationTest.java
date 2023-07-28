package cc.allio.uno.starter.liquibase;

import cc.allio.uno.starter.liquibase.config.UnoLiquibaseAutoConfiguration;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.Environment;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 迁移单元测试
 *
 * @author jiangwei
 * @date 2022/8/29 11:20
 * @since 1.0.9
 */
public class MigrationTest extends CoreTest {

    @Override
    protected void onEnvBuild() {
        registerComponent(
                UnoLiquibaseAutoConfiguration.class,
                DataSourceAutoConfiguration.class
        );
    }

    @Override
    public Environment supportEnv() {
        // TODO 更改测试类
//        DataSourceProperties dataSourceProperties = new DataSourceProperties();
//        dataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSourceProperties.setUrl("jdbc:mysql://192.168.2.29:3306/migration?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
//        dataSourceProperties.setUsername("root");
//        dataSourceProperties.setPassword("123456");
//        return new EnvironmentFacade(new DataSourceEnvironment(dataSourceProperties));
        return null;
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Override
    protected void onContextClose() throws Throwable {

    }

    /**
     * Test Case: 测试数据源与Liquibase的数量
     */
    @Test
    void testSpringLiquibaseSize() {
        Map<String, DataSource> dataSourceMap = getContext().getBeansOfType(DataSource.class);
        Map<String, SpringLiquibase> liquibaseMap = getContext().getBeansOfType(SpringLiquibase.class);
        assertEquals(dataSourceMap.size(), liquibaseMap.size());
    }

}
