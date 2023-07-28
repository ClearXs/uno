package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.data.orm.sql.SQLBinaryCondition;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.TokenOperator;
import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class QueryOperatorTest extends BaseTestCase {

    @Test
    void testSelect() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.selectAll().getSQL();
        assertEquals("SELECT *", sql);
    }

    @Test
    void testFunc() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.select("z")
                .min("z")
                .getSQL();
        assertEquals("SELECT z, MIN(z)", sql);
    }

    @Test
    void testSimpleWhere() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.select("z").from("test").like("x", "zxc").getSQL();
        assertEquals("SELECT z\n" +
                "FROM test\n" +
                "WHERE x LIKE 'zxc'", sql);
    }

    @Test
    void testSubTable() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.leftJoin(Table.of("dual1"), Table.of("dual2"), SQLBinaryCondition.of("dual1.cc", "dual2.aa", TokenOperator.EQUALITY))
                .leftJoinThen("dual", "dual3", SQLBinaryCondition.of("dual.xx", "dual3.xx", TokenOperator.EQUALITY))
                .getSQL();
        assertEquals("SELECT \n" +
                "FROM (dual1\n" +
                "\tLEFT JOIN dual2 ON dual1.cc = dual2.aa) AS dual\n" +
                "\tLEFT JOIN dual3 dual3 ON dual.xx = dual3.xx", sql);
    }

    @Test
    void testOrder() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.select("a")
                .from("dual")
                .orderBy("a", OrderCondition.DESC)
                .getSQL();
        assertEquals("SELECT a\n" +
                "FROM dual\n" +
                "ORDER BY a DESC", sql);
    }

    @Test
    void testGroup() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.select("z")
                .from("dual")
                .groupByOne("z")
                .getSQL();
        assertEquals("SELECT z\n" +
                "FROM dual\n" +
                "GROUP BY z", sql);
    }

    @Test
    void testLimit() {
        DruidSQLQueryOperator operator = new DruidSQLQueryOperator(DBType.H2);
        String sql = operator.select("z")
                .from("dual")
                .page(1L, 10L)
                .getSQL();
        assertEquals("SELECT z\n" +
                "FROM dual\n" +
                "LIMIT 10, 0", sql);
    }
}
