package cc.allio.uno.starter.liquibase;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.StringUtils;
import com.google.auto.service.AutoService;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.function.Predicate;

/**
 * HikariDataSource数据源适配器
 *
 * @author jiangwei
 * @date 2022/8/29 13:05
 * @since 1.0.9
 */
@AutoService(LiquibaseDataSourceAdapter.class)
public class HikariDataSourceAdapter extends BaseLiquibaseDataSourceAdapter {
    @Override
    public String dbType(DataSource dataSource) {
        // org.postgresql.Driver
        // com.mysql.cj.jdbc.Driver
        // org.h2.Driver
        // com.microsoft.sqlserver.jdbc.SQLServerDriver
        // oracle.jdbc.driver.OracleDriver
        String driverClassName = ((HikariDataSource) dataSource).getDriverClassName();
        if (StringUtils.isEmpty(driverClassName)) {
            throw new NullPointerException(String.format("DataSource Driver Class is empty %s", dataSource));
        }
        String maybeDriverClassName = driverClassName.split(StringPool.DOT)[1];

        // sqlserver做特殊处理
        if (maybeDriverClassName.equals("microsoft")) {
            return "mssql";
        } else if (maybeDriverClassName.equals("jdbc")) {
            return "oracle";
        }
        return maybeDriverClassName;
    }

    @Override
    public Predicate<Class<? extends DataSource>> isAdapter() {
        return HikariDataSource.class::isAssignableFrom;
    }
}
