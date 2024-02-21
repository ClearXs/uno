package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.test.model.Operators;
import cc.allio.uno.data.test.model.User;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLUpdateOperatorTest extends BaseTestCase {

    @Test
    void testSimplePojoInsert() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        User user = new User();
        user.setName("a");
        String sql = updateOperator.from(User.class).updatePojo(user).getDSL();
        assertEquals("UPDATE t_users\n" +
                "SET create_user = NULL, create_dept = NULL, create_time = NULL, update_user = NULL, update_time = NULL, is_deleted = NULL, name = 'a', role_id = NULL", sql);
    }

    @Test
    void testUpdateWherePrepareValue() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        updateOperator.eq("eq1", "eq1");
        updateOperator.update("t11", "t11");

        // 交替
        updateOperator.eq("eq2", "eq2");
        updateOperator.update("t12", "t12");

        // 循环10次 update
        for (int i = 0; i < 10; i++) {
            updateOperator.update("t12", "t12");
        }

        // 循环5次 eq
        for (int i = 0; i < 5; i++) {
            updateOperator.eq("eq2", "eq2");
        }

        // 循环10次 update
        for (int i = 0; i < 10; i++) {
            updateOperator.update("t12", "t12");
        }

        System.out.println(updateOperator.getPrepareValues());
    }

    @Test
    void testSimpleUpdate() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        User user = new User();
        user.setName("test");
        String sql = updateOperator.from(User.class).updatePojo(user).getDSL();
        assertEquals("UPDATE t_users\n" +
                "SET create_user = NULL, create_dept = NULL, create_time = NULL, update_user = NULL, update_time = NULL, is_deleted = NULL, name = 'test', role_id = NULL", sql);
    }

    @Test
    void testWhereUpdate() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        String sql = updateOperator.from("dual").update("a", "a").eq("a", "a").getDSL();
        assertEquals("UPDATE PUBLIC.dual\n" +
                "SET a = 'a'\n" +
                "WHERE a = 'a'", sql);
    }

    @Test
    void testComplexWhereUpdate() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        String sql = updateOperator.from("dual").update("a", "a")
                .eq("a", "a")
                .eq("b", "b")
                .between("c", "c1", "c2")
                .in("d", "d1", "d2")
                .isNull("e")
                .getDSL();
        assertEquals("UPDATE PUBLIC.dual\n" +
                "SET a = 'a'\n" +
                "WHERE a = 'a'\n" +
                "\tAND b = 'b'\n" +
                "\tAND c BETWEEN 'c1' AND 'c2'\n" +
                "\tAND d IN ('d1', 'd2')\n" +
                "\tAND e IS NULL", sql);
    }

    @Test
    void testStrictFill() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);

        new Operators(updateOperator)
                .then(() -> {
                    String sql = updateOperator.from("dual")
                            .update("a", null)
                            .strictFill("a", "a")
                            .eq("d", "1")
                            .getDSL();
                    assertEquals("UPDATE PUBLIC.dual\n" +
                            "SET a = 'a'\n" +
                            "WHERE d = '1'", sql);
                })
                .then(() -> {
                    String sql = updateOperator.from("dual")
                            .update("a", null)
                            .update("b", "b")
                            .strictFill("a", "a")
                            .strictFill("b", "3")
                            .strictFill("c", "3")
                            .eq("d", "1")
                            .isNull("e")
                            .getDSL();
                    assertEquals("UPDATE PUBLIC.dual\n" +
                            "SET a = 'a', b = '3', c = '3'\n" +
                            "WHERE d = '1'\n" +
                            "\tAND e IS NULL", sql);
                })
                .eachReset();
    }

    @Test
    void testParse() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        Operators.thenRest(() -> {
            String sql = "UPDATE PUBLIC.dual\n" +
                    "SET a = 'a'\n" +
                    "WHERE a = 'a'\n" +
                    "\tAND b = 'b'";
            String reverse = updateOperator.parse(sql).getDSL();
            assertEquals(sql, reverse);
            return updateOperator;
        });

        Operators.thenRest(() -> {
            String sql = "UPDATE PUBLIC.dual\n" +
                    "SET a = 'a'\n" +
                    "WHERE a = 'a'\n" +
                    "\tAND b = 'b'\n" +
                    "\tAND c BETWEEN 'c1' AND 'c2'\n" +
                    "\tAND d IN ('d1', 'd2')\n" +
                    "\tAND e IS NULL";
            String reverse = updateOperator.parse(sql).getDSL();
            assertEquals(sql, reverse);
            return updateOperator;
        });
    }

    @Test
    void testStrictModelUpdate() {
        UpdateOperator updateOperator = OperatorGroup.getOperator(UpdateOperator.class, OperatorKey.SQL);
        User user = new User();
        user.setId(1L);
        String sql = updateOperator.updatePojo(user).getDSL();
        assertEquals("UPDATE t_users\n" +
                "SET create_user = NULL, create_dept = NULL, create_time = NULL, update_user = NULL, update_time = NULL, is_deleted = NULL, name = NULL, role_id = NULL", sql);
    }
}
