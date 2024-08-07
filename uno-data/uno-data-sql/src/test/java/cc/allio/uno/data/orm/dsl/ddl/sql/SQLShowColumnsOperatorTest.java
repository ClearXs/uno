package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class SQLShowColumnsOperatorTest extends BaseTestCase {

    @Test
    void testMySQLShoWColumns() {
        SQLShowColumnsOperator showColumnsOperator = Operators.getOperator(SQLShowColumnsOperator.class, OperatorKey.SQL, DBType.MYSQL);
        String dsl = showColumnsOperator.from("dual").getDSL();
        assertEquals("SELECT *, TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME\n" +
                "\t, ORDINAL_POSITION, COLUMN_DEFAULT, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH\n" +
                "\t, CHARACTER_OCTET_LENGTH, NUMERIC_PRECISION, NUMERIC_SCALE, DATETIME_PRECISION\n" +
                "FROM INFORMATION_SCHEMA.COLUMNS\n" +
                "WHERE TABLE_NAME = 'dual'", dsl);
    }
}
