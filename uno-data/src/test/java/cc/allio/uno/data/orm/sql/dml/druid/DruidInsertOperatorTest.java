package cc.allio.uno.data.orm.sql.dml.druid;

import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class DruidInsertOperatorTest extends BaseTestCase {

    @Test
    void testInsert() {
        DruidSQLInsertOperator insertOperator = new DruidSQLInsertOperator(DBType.H2);
        String sql = insertOperator.from("test")
                .insert("x", "2")
                .getSQL();
        assertEquals("INSERT INTO test (x)\n" +
                "VALUES ('2')", sql);
        insertOperator.reset();
        sql = insertOperator.
                from(User.class)
                .batchInsertPojos(Lists.newArrayList(new User("21", 2), new User("xc", 2)))
                .getSQL();
        assertEquals("INSERT INTO t_user (name, age)\n" +
                "VALUES ('21', 2), ('xc', 2)", sql);
    }

    @Test
    void testParse() {
        DruidSQLInsertOperator insertOperator = new DruidSQLInsertOperator(DBType.H2);

        insertOperator.parse("INSERT INTO t_user (name, age)\n" +
                "VALUES ('21', 2), ('xc', 2)");
        String sql = insertOperator.getSQL();
    }

    @Data
    @Table(name = "t_user")
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }
}
