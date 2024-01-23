package cc.allio.uno.data.orm.config;

import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.db.DbExecutorProcessor;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(ExecutorInitializerAutoConfiguration.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@ConditionalOnClass({DruidDataSource.class, HikariDataSource.class})
public class DbAutoConfiguration {
    @Bean
    @ConditionalOnClass(SqlSessionFactory.class)
    @ConditionalOnMissingBean
    public DbCommandExecutorLoader dbCommandExecutorLoader(SqlSessionFactory sqlSessionFactory) {
        return new DbCommandExecutorLoader(sqlSessionFactory);
    }

    @Bean
    @ConditionalOnBean(DbCommandExecutorLoader.class)
    public DbExecutorProcessor dbExecutorProcessor() {
        return new DbExecutorProcessor();
    }

}
