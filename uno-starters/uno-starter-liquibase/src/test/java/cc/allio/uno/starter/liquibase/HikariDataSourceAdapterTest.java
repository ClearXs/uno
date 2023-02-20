package cc.allio.uno.starter.liquibase;

import cc.allio.uno.test.BaseTestCase;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.zaxxer.hikari.HikariDataSource;
import oracle.jdbc.driver.OracleDriver;
import org.junit.jupiter.api.Test;
import org.postgresql.Driver;

/**
 * HikariDataSource测试
 *
 * @author jiangwei
 * @date 2022/8/29 13:46
 * @since 1.0.9
 */
class HikariDataSourceAdapterTest extends BaseTestCase {


    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }

    @Test
    void testDbType() {
        HikariDataSourceAdapter dataSourceAdapter = new HikariDataSourceAdapter();
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());

        // 验证mysql
        String dbType = dataSourceAdapter.dbType(hikariDataSource);
        assertEquals("mysql", dbType);

        // 验证Postgresql
        hikariDataSource.setDriverClassName(Driver.class.getName());
        dbType = dataSourceAdapter.dbType(hikariDataSource);
        assertEquals("postgresql", dbType);

        // 验证h2
        hikariDataSource.setDriverClassName(org.h2.Driver.class.getName());
        dbType = dataSourceAdapter.dbType(hikariDataSource);
        assertEquals("h2", dbType);

        // sqlserver
        hikariDataSource.setDriverClassName(SQLServerDriver.class.getName());
        dbType = dataSourceAdapter.dbType(hikariDataSource);
        assertEquals("mssql", dbType);

        // Oracle
        hikariDataSource.setDriverClassName(OracleDriver.class.getName());
        dbType = dataSourceAdapter.dbType(hikariDataSource);
        assertEquals("oracle", dbType);
    }
}
