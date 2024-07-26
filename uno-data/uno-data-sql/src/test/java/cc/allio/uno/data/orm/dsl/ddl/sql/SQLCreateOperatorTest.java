package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
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
        CreateTableOperator<?> createTableOperator = Operators.getOperator(CreateTableOperator.class, OperatorKey.SQL);
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
        CreateTableOperator<?> createTableOperator = Operators.getOperator(CreateTableOperator.class, OperatorKey.SQL);
        String sql = "CREATE TABLE dual (\n" +
                "\tname char(9) PRIMARY KEY NOT NULL UNIQUE\n" +
                ")";
        String literal = createTableOperator.parse(sql).getDSL();
        assertEquals(sql, literal);
    }

    // =================== pg ===================

    @Test
    void testPgCreateComplexTypeTable() {
        CreateTableOperator<?> createTableOperator = Operators.getOperator(CreateTableOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);
        createTableOperator.from("dual");
        ColumnDef d1 = ColumnDef.builder()
                .dslName(DSLName.of("d1"))
                .dataType(DataType.create(DSLType.BIGINT))
                .build();

        ColumnDef d2 = ColumnDef.builder()
                .dslName(DSLName.of("d2"))
                .dataType(DataType.create(DSLType.TIMESTAMP))
                .build();
        ColumnDef d3 = ColumnDef.builder()
                .dslName(DSLName.of("d3"))
                .dataType(DataType.createCharType(DSLType.VARCHAR, 66))
                .build();

        ColumnDef d4 = ColumnDef.builder()
                .dslName(DSLName.of("d4"))
                .dataType(DataType.create(DSLType.SMALLINT))
                .build();
        createTableOperator.columns(d1, d2, d3, d4);
        String dsl = createTableOperator.getDSL();
        assertEquals("CREATE TABLE PUBLIC.dual (\n" +
                "\td1 int8,\n" +
                "\td2 timestamp,\n" +
                "\td3 varchar(66),\n" +
                "\td4 int2\n" +
                ")", dsl);
    }

    @Test
    void testPgParsePojoCreateTableSQL() {
        CreateTableOperator<?> createTableOperator = Operators.getOperator(CreateTableOperator.class, OperatorKey.SQL, DBType.POSTGRESQL);
        String sql = createTableOperator.fromPojo(User.class).getDSL();
        assertEquals("CREATE TABLE t_users (\n" +
                "\tid int8 PRIMARY KEY,\n" +
                "\tcreate_user int8,\n" +
                "\tcreate_dept int8,\n" +
                "\tcreate_time timestamp,\n" +
                "\tupdate_user int8,\n" +
                "\tupdate_time timestamp,\n" +
                "\tis_deleted int4,\n" +
                "\tname varchar(64) NULL,\n" +
                "\trole_id int8 NULL\n" +
                ")", sql);
    }

}
