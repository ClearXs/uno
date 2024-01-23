package cc.allio.uno.data.orm.dsl.dml.sql;

import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.test.model.Operators;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class SQLInsertOperatorTest extends BaseTestCase {

    @Test
    void testInsert() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);
        String sql = insertOperator.from("test")
                .insert("x", "2")
                .getDSL();
        assertEquals("INSERT INTO PUBLIC.test (x)\n" +
                "VALUES ('2')", sql);
        insertOperator.reset();
        sql = insertOperator.
                from(User.class)
                .batchInsertPojos(Lists.newArrayList(new User("21", 2), new User("xc", 2)))
                .getDSL();
        assertEquals("INSERT INTO PUBLIC.t_user (name, age)\n" +
                "VALUES ('21', 2), ('xc', 2)", sql);
    }

    @Test
    void testNotEqualityInsertColumn() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);
        String sql = insertOperator.from("dual")
                .insert("a1", null, "a2", "2")
                .insert("a1", null)
                .getDSL();
        assertEquals("INSERT INTO PUBLIC.dual (a1, a2)\n" +
                "VALUES (NULL, '2'), (NULL, NULL)", sql);
    }

    @Test
    void testNullValueInsert() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);

        Operators.thenRest(() -> {
            String sql = insertOperator.from("dual")
                    .insert("x", null)
                    .getDSL();
            assertEquals("INSERT INTO PUBLIC.dual (x)\n" +
                    "VALUES (NULL)", sql);
            return insertOperator;
        });

        Operators.thenRest(() -> {
            // test multi Null values
            String sql = insertOperator.from("dual")
                    .insert("a", null, "b", null)
                    .getDSL();
            assertEquals("INSERT INTO PUBLIC.dual (a, b)\n" +
                    "VALUES (NULL, NULL)", sql);
            return insertOperator;
        });
    }

    @Test
    void testStrictSingleValueClause() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);
        new Operators(insertOperator)
                .then(() -> {
                    // column不存在
                    String sql = insertOperator.from("dual")
                            .insert("a", null)
                            .strictFill("b", null)
                            .getDSL();

                    assertEquals("INSERT INTO PUBLIC.dual (a, b)\n" +
                            "VALUES (NULL, NULL)", sql);
                })
                .then(() -> {
                    // column存在
                    String sql = insertOperator.from("dual")
                            .insert("a", "a1", "b", null)
                            .strictFill("a", null)
                            .getDSL();
                    assertEquals("INSERT INTO PUBLIC.dual (a, b)\n" +
                            "VALUES (NULL, NULL)", sql);
                })
                .eachReset();
    }

    @Test
    void testStrictMultiValueClause() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);
        new Operators(insertOperator)
                .then(() -> {
                    // column不存在
                    String sql = insertOperator.from("dual")
                            .insert("a1", null, "a2", "2")
                            .insert("a1", null, "a2", "2")
                            .strictFill("a3", "3")
                            .getDSL();
                    assertEquals("INSERT INTO PUBLIC.dual (a1, a2, a3)\n" +
                            "VALUES (NULL, '2', '3'), (NULL, '2', '3')", sql);
                })
                .then(() -> {
                    // column存在
                    String sql = insertOperator.from("dual")
                            .insert("a1", null, "a2", "2")
                            .insert("a1", null, "a2", "2")
                            .strictFill("a1", "1")
                            .getDSL();
                    assertEquals("INSERT INTO PUBLIC.dual (a1, a2)\n" +
                            "VALUES ('1', '2'), ('1', '2')", sql);
                })
                .eachReset();
    }

    @Test
    void testParse() {
        InsertOperator insertOperator = OperatorGroup.getOperator(InsertOperator.class, OperatorKey.SQL);
        String oriSQL = "INSERT INTO PUBLIC.t_user (name, age)\n" +
                "VALUES ('21', 2), ('xc', 2)";
        insertOperator.parse(oriSQL);
        String sql = insertOperator.getDSL();
        assertEquals(oriSQL, sql);
    }

    @Data
    @Table(name = "t_user")
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }
}
