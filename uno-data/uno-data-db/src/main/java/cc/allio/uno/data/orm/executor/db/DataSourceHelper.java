package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DruidDbTypeAdapter;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.function.UnaryOperator;

/**
 * db command executor相关帮助类，包括：
 * <ul>
 *     <li>基于{@link javax.sql.DataSource}获取{@link cc.allio.uno.data.orm.dsl.type.DBType}</li>
 * </ul>
 *
 * @author jiangwei
 * @date 2024/1/25 13:10
 * @since 1.1.7
 */
public final class DataSourceHelper {

    public static <T extends DataSource> T createDataSource(String username, String password, String jdbcUrl) {
        return createDataSource(builder -> (DataSourceBuilder<T>) builder.type(HikariDataSource.class)
                .username(username)
                .password(password)
                .url(jdbcUrl));
    }

    public static <T extends DataSource> T createDataSource(UnaryOperator<DataSourceBuilder<T>> operator) {
        return createDataSource(operator.apply((DataSourceBuilder<T>) DataSourceBuilder.create(ClassLoader.getSystemClassLoader())));
    }

    public static <T extends DataSource> T createDataSource(DataSourceBuilder<T> dataSourceBuilder) {
        return dataSourceBuilder.build();
    }

    /**
     * 获取username
     *
     * @param dataSource dataSource
     * @return username or null
     */
    public static String getUsername(DataSource dataSource) {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            return druidDataSource.getUsername();
        }
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            return hikariDataSource.getUsername();
        }
        return null;
    }

    /**
     * 获取password
     *
     * @param dataSource dataSource
     * @return password or null
     */
    public static String getPassword(DataSource dataSource) {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            return druidDataSource.getPassword();
        }
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            return hikariDataSource.getPassword();
        }
        return null;
    }

    /**
     * 获取连接address
     *
     * @param dataSource dataSource
     * @return address or null
     * @see DBType
     */
    public static String getAddress(DataSource dataSource) {
        String jdbcUrl = null;
        if (dataSource instanceof DruidDataSource druidDataSource) {
            jdbcUrl = druidDataSource.getUrl();
        }
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            jdbcUrl = hikariDataSource.getJdbcUrl();
        }
        if (StringUtils.isBlank(jdbcUrl)) {
            return null;
        }
        int addressIndex = jdbcUrl.indexOf("//");
        if (addressIndex < 0) {
            return null;
        }
        String cropJdbcUrl = jdbcUrl.substring(addressIndex + 2);
        if (StringUtils.isBlank(cropJdbcUrl)) {
            return null;
        }
        int databaseIndex = cropJdbcUrl.indexOf("/");
        if (databaseIndex < 0) {
            return null;
        }
        return cropJdbcUrl.substring(0, databaseIndex);
    }

    /**
     * 获取数据库 name
     *
     * @param dataSource dataSource
     * @return database
     */
    public static String getDatabase(DataSource dataSource) {
        String jdbcUrl = null;
        if (dataSource instanceof DruidDataSource druidDataSource) {
            jdbcUrl = druidDataSource.getUrl();
        }
        if (dataSource instanceof HikariDataSource hikariDataSource) {
            jdbcUrl = hikariDataSource.getJdbcUrl();
        }
        if (StringUtils.isBlank(jdbcUrl)) {
            return null;
        }
        int addressIndex = jdbcUrl.indexOf("//");
        if (addressIndex < 0) {
            return null;
        }
        String cropJdbcUrl = jdbcUrl.substring(addressIndex + 2);
        if (StringUtils.isBlank(cropJdbcUrl)) {
            return null;
        }
        int paramsIndex = cropJdbcUrl.indexOf("?");
        if (paramsIndex > 0) {
            return cropJdbcUrl.substring(0, paramsIndex);
        }
        int slashIndex = cropJdbcUrl.indexOf("/");
        if (slashIndex < 0) {
            return cropJdbcUrl;
        }
        return cropJdbcUrl.substring(slashIndex + 1);
    }

    /**
     * 根据{@link DataSource}获取{@link DBType}
     *
     * @param dataSource dataSource
     * @return Dbtype
     */
    public static DBType getDbType(DataSource dataSource) {
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
    private static DBType fetchDriveClassName(String driveClassName) {
        for (DBType dbType : DBType.ALL_DB_TYPES) {
            if (dbType.getDriverClassName().equals(driveClassName)) {
                return dbType;
            }
        }
        return DBType.H2;
    }


}
