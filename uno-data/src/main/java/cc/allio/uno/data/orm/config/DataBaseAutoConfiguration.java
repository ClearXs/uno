package cc.allio.uno.data.orm.config;

import cc.allio.uno.data.orm.ORMProperties;
import cc.allio.uno.data.orm.sql.*;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.executor.SQLCommandExecutor;
import cc.allio.uno.data.orm.executor.SQLCommandExecutorFactory;
import cc.allio.uno.data.orm.executor.mybatis.MybatisSQLCommandExecutor;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.type.druid.DruidDbTypeAdapter;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static cc.allio.uno.data.orm.executor.SQLCommandExecutor.MYBATIS_SQL_COMMAND_EXECUTOR_KEY;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@ConditionalOnClass({DruidDataSource.class, HikariDataSource.class})
@EnableConfigurationProperties(ORMProperties.class)
public class DataBaseAutoConfiguration implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // druid
        if (dataSource instanceof DruidDataSource) {
            String dbType = ((DruidDataSource) dataSource).getDbType();
            DbType druidDbType = DbType.of(dbType);
            DBType systemDbType = DruidDbTypeAdapter.getInstance().reversal(druidDbType);
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, systemDbType.getName());
        }
        // hikari
        if (dataSource instanceof HikariDataSource) {
            String driverClassName = ((HikariDataSource) dataSource).getDriverClassName();
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, parseDriveClassNameForDBtype(driverClassName).getName());
        }
        // pg
        if (dataSource instanceof PGSimpleDataSource) {
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, DBType.POSTGRESQL.getName());
        }
        // mysql
        if (dataSource instanceof MysqlDataSource) {
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, DBType.MYSQL.getName());
        }
        // mssql
        if (dataSource instanceof SQLServerDataSource) {
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, DBType.SQLSERVER.getName());
        }
    }

    @Bean
    @ConditionalOnProperty(name = OperatorMetadata.OPERATOR_METADATA_KEY, havingValue = "druid")
    public DruidOperatorMetadata druidOperatorMetadata() {
        Envs.setProperty(OperatorMetadata.OPERATOR_METADATA_KEY, OperatorMetadata.DRUID_OPERATOR_KEY.getKey());
        return SQLOperatorFactory.getSystemOperatorMetadata();
    }

    @Bean
    @ConditionalOnProperty(name = OperatorMetadata.OPERATOR_METADATA_KEY, havingValue = "local")
    @ConditionalOnMissingBean
    public LocalOperatorMetadata localOperatorMetadata() {
        Envs.setProperty(OperatorMetadata.OPERATOR_METADATA_KEY, OperatorMetadata.LOCAL_OPERATOR_KEY.getKey());
        return SQLOperatorFactory.getSystemOperatorMetadata();
    }

    @Bean
    @ConditionalOnProperty(name = OperatorMetadata.OPERATOR_METADATA_KEY, havingValue = "sharding-sphere")
    @ConditionalOnMissingBean
    public ShardingSphereOperatorMetadata shardingSphereOperatorMetadata() {
        Envs.setProperty(OperatorMetadata.OPERATOR_METADATA_KEY, OperatorMetadata.SHARDING_SPHERE_KEY.getKey());
        return SQLOperatorFactory.getSystemOperatorMetadata();
    }

    @Bean
    @ConditionalOnClass(SqlSessionFactory.class)
    @ConditionalOnMissingBean
    public MybatisSQLCommandExecutor mybatisSQLCommandExecutor(SqlSessionFactory sqlSessionFactory) {
        Envs.setProperty(SQLCommandExecutor.SQL_EXECUTOR_TYPE_KEY, MYBATIS_SQL_COMMAND_EXECUTOR_KEY.getKey());
        return SQLCommandExecutorFactory.create(MYBATIS_SQL_COMMAND_EXECUTOR_KEY, new Object[]{sqlSessionFactory.getConfiguration()});
    }

    /**
     * 解析driveClassName为DBType
     *
     * @param driveClassName driveClassName
     * @return DBType or default h2
     */
    private DBType parseDriveClassNameForDBtype(String driveClassName) {
        if ("com.mysql.cj.jdbc.Driver".equals(driveClassName) || "com.mysql.jdbc.Driver".equals(driveClassName)) {
            return DBType.MYSQL;
        } else if ("org.postgresql.Driver".equals(driveClassName)) {
            return DBType.POSTGRESQL;
        }
        return DBType.H2;
    }
}
