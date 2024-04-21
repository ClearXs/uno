package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.test.model.Operators;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLQueryOperatorTest extends BaseTestCase {

    @Test
    void testSelect() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.selectAll().getDSL();
        assertEquals("SELECT *", sql);
    }

    @Test
    void testFunc() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z")
                .min("z")
                .getDSL();
        assertEquals("SELECT z, MIN(z)", sql);
    }

    @Test
    void testSimpleWhere() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").like("x", "zxc").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE x LIKE 'zxc'", sql);
    }

    @Test
    void testMultiWhere() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").like("a", "a").eq("b", "b").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE a LIKE 'a'\n" +
                "\tAND b = 'b'", sql);
    }

    @Test
    void testLogicPredicateOr() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").or().eq("a", "a").eq("b", "b").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE a = 'a'\n" +
                "\tOR b = 'b'", sql);
    }

    @Test
    void testMultiLogicPredicate() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").or().eq("a", "a").eq("b", "b").and().eq("d", "d").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE (a = 'a'\n" +
                "\t\tOR b = 'b')\n" +
                "\tAND d = 'd'", sql);
    }

    @Test
    void testSubTable() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.leftJoin(Table.of("dual1"), Table.of("dual2"), BinaryCondition.of("dual1.cc", "dual2.aa", TokenOperator.EQUALITY))
                .leftJoinThen("dual", "dual3", BinaryCondition.of("dual.xx", "dual3.xx", TokenOperator.EQUALITY))
                .getDSL();
        assertEquals("SELECT \n" +
                "FROM (dual1\n" +
                "\tLEFT JOIN PUBLIC.dual2 ON dual1.cc = dual2.aa) AS dual\n" +
                "\tLEFT JOIN PUBLIC.dual3 ON dual.xx = dual3.xx", sql);
    }

    @Test
    void testOrder() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("a")
                .from("dual")
                .orderBy("a", OrderCondition.DESC)
                .getDSL();
        assertEquals("SELECT a\n" +
                "FROM PUBLIC.dual\n" +
                "ORDER BY a DESC", sql);
    }

    @Test
    void testGroup() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z")
                .from("dual")
                .groupByOne("z")
                .getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "GROUP BY z", sql);
    }

    @Test
    void testLimit() {
        Operators.thenRest(() -> {
            SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
            String sql = operator.select("z")
                    .from("dual")
                    .page(1L, 10L)
                    .getDSL();
            assertEquals("SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "LIMIT 0, 10", sql);
            return operator;
        });

        // test pg
        Operators.thenRest(() -> {
            SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
            String sql = operator.select("z")
                    .from("dual")
                    .page(1L, 10L)
                    .getDSL();
            assertEquals("SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "LIMIT 0, 10", sql);
            return operator;
        });
    }

    @Test
    void testIn() {
        Operators.thenRest(() -> {
            SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
            String sql = operator.select("z")
                    .from("dual")
                    .in("t1", "2", "2")
                    .getDSL();
            assertEquals("SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "WHERE t1 IN ('2', '2')", sql);
            return operator;
        });

    }

    @Test
    void testParse() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        Operators.thenRest(() -> {
            String s1 = "SELECT *\n" +
                    "FROM dual";
            String s1p = operator.parse(s1).getDSL();
            assertEquals(s1, s1p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s2 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "WHERE x LIKE 'zxc'";
            String s2p = operator.parse(s2).getDSL();
            assertEquals(s2, s2p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s3 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "WHERE a LIKE 'a'\n" +
                    "\tAND b = 'b'";
            String s3p = operator.parse(s3).getDSL();
            assertEquals(s3, s3p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s4 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "WHERE a = 'a'\n" +
                    "\tOR b = 'b'";
            String s4p = operator.parse(s4).getDSL();
            assertEquals(s4, s4p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s5 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "WHERE (a = 'a'\n" +
                    "\t\tOR b = 'b')\n" +
                    "\tAND d = 'd'";
            String s5p = operator.parse(s5).getDSL();
            assertEquals(s5, s5p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s7 = "SELECT a\n" +
                    "FROM PUBLIC.dual\n" +
                    "ORDER BY a DESC";
            String s7p = operator.parse(s7).getDSL();
            assertEquals(s7, s7p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s8 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "GROUP BY z";
            String s8p = operator.parse(s8).getDSL();
            assertEquals(s8, s8p);
            return operator;
        });

        Operators.thenRest(() -> {
            String s9 = "SELECT z\n" +
                    "FROM PUBLIC.dual\n" +
                    "LIMIT 10, 0";
            String s9p = operator.parse(s9).getDSL();
            assertEquals(s9, s9p);
            return operator;
        });
    }

    @Test
    void testRecursive() {
        String sql = "WITH RECURSIVE biz_tree AS (SELECT *\n" +
                "FROM org\n" +
                " UNION (SELECT sub.* FROM ((SELECT *\n" +
                "FROM org\n" +
                ") sub INNER JOIN biz_tree P ON P.ID = sub.parent_id))) SELECT * FROM biz_tree";
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);
        String dsl = operator.parse(sql).getDSL();
        assertEquals("WITH RECURSIVE biz_tree AS (\n" +
                "\t\tSELECT *\n" +
                "\t\tFROM org\n" +
                "\t\tUNION\n" +
                "\t\tSELECT sub.*\n" +
                "\t\tFROM (\n" +
                "\t\t\tSELECT *\n" +
                "\t\t\tFROM org\n" +
                "\t\t) sub\n" +
                "\t\t\tINNER JOIN biz_tree P ON P.ID = sub.parent_id\n" +
                "\t)\n" +
                "SELECT *\n" +
                "FROM biz_tree", dsl);
    }

    @Test
    void testPeelWhere() {
        SQLQueryOperator operator = OperatorGroup.getOperator(SQLQueryOperator.class, OperatorKey.SQL);

        String whereDSL = operator.eq("a", "a").getWhereDSL();
        assertEquals("a = 'a'", whereDSL);
    }
}
