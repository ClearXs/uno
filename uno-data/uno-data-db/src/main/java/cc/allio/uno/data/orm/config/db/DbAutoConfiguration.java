package cc.allio.uno.data.orm.config.db;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.CommandExecutorRegistry;
import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.data.orm.executor.ExecutorOptionsBuilder;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutor;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutorProcessor;
import cc.allio.uno.data.orm.executor.db.DbMybatisConfiguration;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * configuration for db
 *
 * @author j.x
 * @since 1.1.4
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ExecutorInitializerAutoConfiguration.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@ConditionalOnClass({DruidDataSource.class, HikariDataSource.class})
@EnableConfigurationProperties(DbProperties.class)
public class DbAutoConfiguration {

    @Bean
    @ConditionalOnClass(MybatisSqlSessionFactoryBean.class)
    @ConditionalOnMissingBean
    public DbCommandExecutorLoader dbCommandExecutorLoader(SqlSessionFactory sqlSessionFactory) {
        return new DbCommandExecutorLoader(new DbMybatisConfiguration(sqlSessionFactory.getConfiguration()));
    }

    @Bean
    @ConditionalOnBean(DbCommandExecutorLoader.class)
    public DbCommandExecutorProcessor dbExecutorProcessor() {
        return new DbCommandExecutorProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(CommandExecutorRegistry.class)
    @ConditionalOnProperty(prefix = "allio.uno.data.db", name = "enabled", havingValue = "true")
    public DbCommandExecutor defaultDbCommandExecutor(DbCommandExecutorLoader commandExecutorLoader,
                                                      DbProperties dbProperties,
                                                      CommandExecutorRegistry commandExecutorRegistry,
                                                      ObjectProvider<List<Interceptor>> interceptorProvider) {
        List<Interceptor> interceptors = interceptorProvider.getIfAvailable(List::of);
        DbProperties.DbType dbType = dbProperties.getDbType();
        DBType systemDBType = switch (dbType) {
            case MYSQL -> DBType.MYSQL;
            case ORACLE -> DBType.ORACLE;
            case SQLITE -> DBType.SQLITE;
            case MARIADB -> DBType.MARIADB;
            case SQLSERVER -> DBType.SQLSERVER;
            case DB2 -> DBType.DB2;
            case POSTGRESQL -> DBType.POSTGRESQL;
            case OPEN_GAUSS -> DBType.OPEN_GAUSS;
            case null, default -> DBType.H2;
        };
        ExecutorOptions executorOptions =
                ExecutorOptionsBuilder.create(systemDBType, dbType.name())
                        .address(dbProperties.getAddress())
                        .username(dbProperties.getUsername())
                        .password(dbProperties.getPassword())
                        .executorKey(ExecutorKey.DB)
                        .operatorKey(OperatorKey.SQL)
                        .interceptors(interceptors)
                        .build();
        return commandExecutorRegistry.register(executorOptions, () -> commandExecutorLoader.load(executorOptions), false);
    }

}
