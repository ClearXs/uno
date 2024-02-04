package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class DataSourceHelperTest extends BaseTestCase {
    DataSource postgreSQLDataSource = DataSourceHelper.createDataSource("", "", "jdbc:postgresql://localhost:5432/test");

    @Test
    void testGetAddressByPostgreSQL() {

        String address = DataSourceHelper.getAddress(postgreSQLDataSource);

        assertEquals("localhost:5432", address);
    }

    @Test
    void testGetDatabaseByPostgreSQL() {
        String database = DataSourceHelper.getDatabase(postgreSQLDataSource);

        assertEquals("test", database);
    }
}
