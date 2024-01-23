package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.type.DSLType;
import cc.allio.uno.data.orm.dsl.type.DataType;
import cc.allio.uno.data.test.model.User;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLCreateOperatorTest extends BaseTestCase {

    @Test
    void testCreateTable() {
        CreateTableOperator createTableOperator = OperatorGroup.getOperator(CreateTableOperator.class, OperatorKey.SQL);
        String sql = createTableOperator.from("dual")
                .column(
                        ColumnDef.builder()
                                .dslName(DSLName.of("name"))
                                .dataType(DataType.createCharType(DSLType.CHAR, 9))
                                .isPk(true)
                                .isNonNull(true)
                                .isUnique(true)
                                .build())
                .getDSL();
        assertEquals("CREATE TABLE PUBLIC.dual (\n" +
                "\tname char(9) PRIMARY KEY NOT NULL UNIQUE\n" +
                ")", sql);
    }

    @Test
    void testParseCreateTableSQL() {
        CreateTableOperator createTableOperator = OperatorGroup.getOperator(CreateTableOperator.class, OperatorKey.SQL);
        String sql = "CREATE TABLE dual (\n" +
                "\tname char(9) PRIMARY KEY NOT NULL UNIQUE\n" +
                ")";
        String literal = createTableOperator.parse(sql).getDSL();
        assertEquals(sql, literal);
    }

    @Test
    void testParsePojoCreateTableSQL() {
        CreateTableOperator createTableOperator = OperatorGroup.getOperator(CreateTableOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);

        String sql = createTableOperator.fromPojo(User.class).getDSL();

        assertEquals("CREATE TABLE t_users (\n" +
                "\tid bigint PRIMARY KEY,\n" +
                "\tcreate_user bigint,\n" +
                "\tcreate_dept bigint,\n" +
                "\tcreate_time timestamp,\n" +
                "\tupdate_user bigint,\n" +
                "\tupdate_time timestamp,\n" +
                "\tis_deleted int,\n" +
                "\tname varchar(64) NULL,\n" +
                "\trole_id bigint NULL\n" +
                ")", sql);
    }

}
