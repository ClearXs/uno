package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.sql.dml.SQLDeleteOperator;
import cc.allio.uno.data.test.model.User;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLDeleteOperatorTest extends BaseTestCase {

    @Test
    void testSimplePojoInsert() {
        SQLDeleteOperator deleteOperator = Operators.getOperator(SQLDeleteOperator.class, OperatorKey.SQL);
        String sql = deleteOperator.from(User.class).from(User.class).getDSL();
        assertEquals("DELETE FROM PUBLIC.t_users", sql);
    }

    @Test
    void testComplexCondition() {
        SQLDeleteOperator deleteOperator = Operators.getOperator(SQLDeleteOperator.class, OperatorKey.SQL);
        String sql = deleteOperator.from("dual")
                .eq("a", "a")
                .or()
                .in("b", "b1", "b2")
                .and()
                .between("c", "c1", "c2")
                .isNull("e")
                .getDSL();
        assertEquals("DELETE FROM PUBLIC.dual\n" +
                "WHERE (a = 'a'\n" +
                "\t\tOR b IN ('b1', 'b2'))\n" +
                "\tAND c BETWEEN 'c1' AND 'c2'\n" +
                "\tAND e IS NULL", sql);
    }

    @Test
    void testParseSQL() {
        SQLDeleteOperator deleteOperator = Operators.getOperator(SQLDeleteOperator.class, OperatorKey.SQL);
        cc.allio.uno.data.test.model.Operators.thenRest(() -> {
            String sql = "DELETE FROM PUBLIC.dual\n" +
                    "WHERE (a = 'a'\n" +
                    "\t\tOR b IN ('b1', 'b2'))\n" +
                    "\tAND c BETWEEN 'c1' AND 'c2'\n" +
                    "\tAND e IS NULL";
            String reversal = deleteOperator.parse(sql).getDSL();
            assertEquals(sql, reversal);
            return deleteOperator;
        });
    }
}

