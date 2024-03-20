package cc.allio.uno.data.orm.config.db;

import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutorLoader;
import cc.allio.uno.data.orm.executor.db.DbCommandExecutorProcessor;
import cc.allio.uno.data.orm.executor.db.DbMybatisConfiguration;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
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
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
@ConditionalOnClass({DruidDataSource.class, HikariDataSource.class})
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
}
