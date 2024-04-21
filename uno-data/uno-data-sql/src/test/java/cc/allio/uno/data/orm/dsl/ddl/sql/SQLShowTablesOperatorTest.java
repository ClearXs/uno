package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.sql.ddl.SQLShowTablesOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLShowTablesOperatorTest extends BaseTestCase {

    @Test
    void testSQLLByH2() {
        SQLShowTablesOperator operator = OperatorGroup.getOperator(SQLShowTablesOperator.class, OperatorKey.SQL);
        String sql = operator.getDSL();
        assertEquals("SELECT TABLE_CATALOG AS CATALOG, TABLE_SCHEMA AS SCHEMA, TABLE_NAME AS NAME, TABLE_TYPE AS TYPE\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'PUBLIC'\n" +
                "\tAND TABLE_TYPE = 'BASE TABLE'", sql);
    }

    @Test
    void testSQLLByMySQL() {
        SQLShowTablesOperator operator = OperatorGroup.getOperator(SQLShowTablesOperator.class, OperatorKey.SQL, DBType.MYSQL);
        String sql = operator.database("da").getDSL();
        assertEquals("SELECT TABLE_CATALOG AS CATALOG, TABLE_SCHEMA AS SCHEMA, TABLE_NAME AS NAME, TABLE_TYPE AS TYPE\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'da'\n" +
                "\tAND TABLE_TYPE = 'BASE TABLE'", sql);
    }


    @Test
    void testSQLLByPostgreSQL() {
        SQLShowTablesOperator operator = OperatorGroup.getOperator(SQLShowTablesOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);
        String sql = operator.getDSL();
        assertEquals("SELECT TABLE_CATALOG AS CATALOG, TABLE_SCHEMA AS SCHEMA, TABLE_NAME AS NAME, TABLE_TYPE AS TYPE\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'PUBLIC'\n" +
                "\tAND TABLE_TYPE = 'BASE TABLE'", sql);
    }

    @Test
    void testDualShowTable() {
        SQLShowTablesOperator operator = OperatorGroup.getOperator(SQLShowTablesOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);
        String sql = operator.from("dual").getDSL();
        assertEquals("SELECT TABLE_CATALOG AS CATALOG, TABLE_SCHEMA AS SCHEMA, TABLE_NAME AS NAME, TABLE_TYPE AS TYPE\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'PUBLIC'\n" +
                "\tAND TABLE_TYPE = 'BASE TABLE'\n" +
                "\tAND TABLE_NAME = 'dual'", sql);
    }

    @Test
    void testCompositeShowTable() {
        SQLShowTablesOperator operator = OperatorGroup.getOperator(SQLShowTablesOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);
        String sql = operator.from("dualA").from("dualB").database("da").schema("db").getDSL();
        assertEquals("SELECT TABLE_CATALOG AS CATALOG, TABLE_SCHEMA AS SCHEMA, TABLE_NAME AS NAME, TABLE_TYPE AS TYPE\n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_SCHEMA = 'db'\n" +
                "\tAND TABLE_TYPE = 'BASE TABLE'\n" +
                "\tAND TABLE_NAME IN ('dual_a', 'dual_b')", sql);
    }
}
