package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.test.model.Operators;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLQueryOperatorTest extends BaseTestCase {

    @Test
    void testSelect() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.selectAll().getDSL();
        assertEquals("SELECT *", sql);
    }

    @Test
    void testFunc() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z")
                .min("z")
                .getDSL();
        assertEquals("SELECT z, MIN(z)", sql);
    }

    @Test
    void testSimpleWhere() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").like("x", "zxc").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE x LIKE 'zxc'", sql);
    }

    @Test
    void testMultiWhere() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").like("a", "a").eq("b", "b").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE a LIKE 'a'\n" +
                "\tAND b = 'b'", sql);
    }

    @Test
    void testLogicPredicateOr() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").or().eq("a", "a").eq("b", "b").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE a = 'a'\n" +
                "\tOR b = 'b'", sql);
    }

    @Test
    void testMultiLogicPredicate() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z").from("dual").or().eq("a", "a").eq("b", "b").and().eq("d", "d").getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "WHERE (a = 'a'\n" +
                "\t\tOR b = 'b')\n" +
                "\tAND d = 'd'", sql);
    }

    @Test
    void testSubTable() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
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
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
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
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
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
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
        String sql = operator.select("z")
                .from("dual")
                .page(1L, 10L)
                .getDSL();
        assertEquals("SELECT z\n" +
                "FROM PUBLIC.dual\n" +
                "LIMIT 10, 0", sql);
    }

    @Test
    void testParse() {
        QueryOperator operator = OperatorGroup.getOperator(QueryOperator.class, OperatorKey.SQL);
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

        // druid failed
//        Operators.thenRest(() -> {
//            String s6 = "SELECT \n" +
//                    "FROM (dual1\n" +
//                    "\tLEFT JOIN PUBLIC.dual2 ON dual1.cc = dual2.aa) AS dual\n" +
//                    "\tLEFT JOIN PUBLIC.dual3 ON dual.xx = dual3.xx";
//            String s6p = operator.parse(s6).getDSL();
//            assertEquals(s6, s6p);
//            return operator;
//        });

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

}
