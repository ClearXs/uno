package cc.allio.uno.starter.liquibase;

import com.alibaba.druid.pool.DruidDataSource;
import cc.allio.uno.core.util.template.GenericTokenParser;
import cc.allio.uno.test.BaseTestCase;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.enums.SeataMode;
import org.junit.jupiter.api.Test;

class DynamicRoutingDataSourceAdapterTest extends BaseTestCase {

    DynamicRoutingDataSource dataSource;

    DynamicRoutingDataSourceAdapter adapter;

    ItemDataSource master;

    ItemDataSource second;

    @Override
    protected void onInit() throws Throwable {
        dataSource = new DynamicRoutingDataSource();
        DruidDataSource masterDruidDataSource = new DruidDataSource();
        masterDruidDataSource.setDbType("postgres");
        master = new ItemDataSource("master", masterDruidDataSource, masterDruidDataSource, false, false, SeataMode.AT);
        dataSource.addDataSource("master", master);
        DruidDataSource secondDruidDataSource = new DruidDataSource();
        secondDruidDataSource.setDbType("mysql");
        second = new ItemDataSource("second", secondDruidDataSource, secondDruidDataSource, false, false, SeataMode.AT);
        dataSource.addDataSource("second", second);
        adapter = new DynamicRoutingDataSourceAdapter();
    }

    @Test
    void testDbType() {
        assertEquals("postgres", adapter.dbType(master));
        assertEquals("mysql", adapter.dbType(second));
    }

    @Test
    void testChangeFilePath() {
        GenericTokenParser masterParser = adapter.getDbParser().apply(master);
        assertEquals("classpath:db/migrations/postgres", adapter.changeLogPath(master, masterParser));
        GenericTokenParser secondParser = adapter.getDbParser().apply(second);
        assertEquals("classpath:db/migrations/mysql", adapter.changeLogPath(second, secondParser));
    }

    @Test
    void testChangeName() {
        GenericTokenParser masterParser = adapter.getDbParser().apply(master);
        assertEquals("db_migration-master", adapter.changeLogName(master, masterParser));
        GenericTokenParser secondParser = adapter.getDbParser().apply(second);
        assertEquals("db_migration-second", adapter.changeLogName(second, secondParser));
    }

    @Test
    void testGetChangeLog() {
        assertEquals("classpath:db/migrations/postgres/db_migration-master.yaml", adapter.getChangeLog(master));
        assertEquals("classpath:db/migrations/mysql/db_migration-second.yaml", adapter.getChangeLog(second));
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
