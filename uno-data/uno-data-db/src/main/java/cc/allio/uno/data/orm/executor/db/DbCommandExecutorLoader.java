package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.auto.service.AutoService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DatabaseDriver;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实例化{@link CommandExecutor}
 *
 * @author j.x
 * @date 2024/1/10 18:27
 * @since 1.1.7
 */
@AutoService(CommandExecutorLoader.class)
public class DbCommandExecutorLoader extends BaseCommandExecutorLoader<DbCommandExecutor> {

    private final DbMybatisConfiguration configuration;
    private final AtomicInteger createCount = new AtomicInteger(0);

    public DbCommandExecutorLoader() {
        this.configuration = new DbMybatisConfiguration(new Configuration());
    }

    public DbCommandExecutorLoader(DbMybatisConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public DbCommandExecutor onLoad(List<Interceptor> interceptors) {
        DataSource dataSource = configuration.getEnvironment().getDataSource();
        DBType dbType = DataSourceHelper.getDbType(dataSource);
        String username = DataSourceHelper.getUsername(dataSource);
        String password = DataSourceHelper.getPassword(dataSource);
        String address = DataSourceHelper.getAddress(dataSource);
        String database = DataSourceHelper.getDatabase(dataSource);
        ExecutorOptions executorOptions = ExecutorOptionsBuilder
                .create(dbType, "default_" + dbType.getName())
                .executorKey(ExecutorKey.DB)
                .operatorKey(OperatorKey.SQL)
                .username(username)
                .password(password)
                .address(address)
                .database(database)
                .interceptors(interceptors)
                .systemDefault(true)
                .build();
        return new DbCommandExecutor(executorOptions, configuration);
    }

    @Override
    public DbCommandExecutor onLoad(ExecutorOptions executorOptions) {
        DBType dbType = executorOptions.getDbType();
        String jdbcUrl = dbType.parseTemplate(executorOptions.getAddress(), executorOptions.getDatabase());
        HikariDataSource dataSource =
                DataSourceBuilder.create(ClassLoader.getSystemClassLoader())
                        .type(HikariDataSource.class)
                        .driverClassName( DatabaseDriver.fromJdbcUrl(jdbcUrl).getDriverClassName())
                        .url(jdbcUrl)
                        .username(executorOptions.getUsername())
                        .password(executorOptions.getPassword())
                        .build();
        dataSource.setAutoCommit(true);
        dataSource.setConnectionTimeout(Duration.ofSeconds(10).toMillis());
        dataSource.setMaximumPoolSize(2);
        dataSource.setMaxLifetime(Duration.ofSeconds(30).toMillis());
        DbMybatisConfiguration newConfiguration = configuration.copy();
        Environment environment = new Environment("DbCommandExecutorLoader" + createCount.getAndIncrement(), new SpringManagedTransactionFactory(), dataSource);
        newConfiguration.setEnvironment(environment);
        return new DbCommandExecutor(executorOptions, newConfiguration);
    }

    @Override
    public boolean match(DBType dbType) {
        return DBType.DBCategory.RELATIONAL == dbType.getCategory();
    }

}
