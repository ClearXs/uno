package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.jdbc.DataSourceBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RunTest
@MybatisEnv
class DbExecutorDynamicCreateTest extends BaseTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Test
    void testCreateByH2DataSource() {
        JdbcConnectionDetails connectionDetails = new JdbcConnectionDetailsImpl(null, null, "jdbc:h2:mem:test-db;IGNORECASE=TRUE");
        AggregateCommandExecutor commandExecutor = getCommandExecutor(connectionDetails);
        List<Map<String, Object>> maps = commandExecutor.queryListMap(o -> o.select("1"));
        assertNotNull(maps);
    }

    private HikariDataSource getHakariDataSource(JdbcConnectionDetails connectionDetails) {
        HikariDataSource dataSource = DataSourceBuilder.create(getClass().getClassLoader())
                .type(HikariDataSource.class)
                .driverClassName(connectionDetails.getDriverClassName())
                .url(connectionDetails.getJdbcUrl())
                .username(connectionDetails.getUsername())
                .password(connectionDetails.getPassword())
                .build();
        dataSource.setMaximumPoolSize(1);
        return dataSource;
    }

    private AggregateCommandExecutor getCommandExecutor(JdbcConnectionDetails connectionDetails) {
        HikariDataSource dataSource = getHakariDataSource(connectionDetails);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        DbMybatisConfiguration mybatisConfiguration = new DbMybatisConfiguration(configuration);
        Environment environment = new Environment("h2", new SpringManagedTransactionFactory(), dataSource);
        mybatisConfiguration.setEnvironment(environment);
        DbCommandExecutorLoader loader = new DbCommandExecutorLoader(mybatisConfiguration);
        return loader.load(Collections.emptyList());
    }
}
