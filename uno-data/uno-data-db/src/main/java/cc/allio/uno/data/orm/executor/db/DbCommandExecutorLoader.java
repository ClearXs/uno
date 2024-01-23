package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ExecutorKey;
import cc.allio.uno.data.orm.executor.ExecutorLoader;
import cc.allio.uno.data.orm.executor.ExecutorOptions;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.util.List;

/**
 * 实例化{@link CommandExecutor}
 *
 * @author jiangwei
 * @date 2024/1/10 18:27
 * @since 1.1.6
 */
public class DbCommandExecutorLoader implements ExecutorLoader {

    private final SqlSessionFactory sqlSessionFactory;

    public DbCommandExecutorLoader(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public CommandExecutor load(List<Interceptor> interceptors) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        ExecutorOptions executorOptions = new ExecutorOptions();
        executorOptions.addInterceptors(interceptors);
        executorOptions.setExecutorKey(ExecutorKey.DB);
        executorOptions.setOperatorKey(OperatorKey.SQL);
        DataSource dataSource = configuration.getEnvironment().getDataSource();
        DBType dbType = getDbTypeByDataSource(dataSource);
        executorOptions.setDbType(dbType);
        return new DbCommandExecutor(executorOptions, configuration);
    }


    public DBType getDbTypeByDataSource(DataSource dataSource) {
        DBType dbType = null;
        // druid
        if (dataSource instanceof DruidDataSource druidDataSource) {
            DbType druidDbType = DbType.of(druidDataSource.getDbType());
            dbType = DruidDbTypeAdapter.getInstance().reverse(druidDbType);
        }
        // hikari
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            String driverClassName = hikariDataSource.getDriverClassName();
            dbType = fetchDriveClassName(driverClassName);
        }
        if (dbType == null) {
            dbType = DBType.H2;
        }
        Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, dbType.getName());
        return dbType;
    }


    /**
     * 解析driveClassName为DBType
     *
     * @param driveClassName driveClassName
     * @return DBType or default h2
     */
    private DBType fetchDriveClassName(String driveClassName) {
        for (DBType dbType : DBType.ALL_DB_TYPES) {
            if (dbType.getDriverClassName().equals(driveClassName)) {
                return dbType;
            }
        }
        return DBType.H2;
    }
}
