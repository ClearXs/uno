package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.sql.ddl.DDLSQLSupport;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import org.junit.jupiter.api.Test;

public class DDLSQLSupportTest extends BaseTestCase {

    @Test
    void testCreateTableCommentStatement() {
        Table table = new Table();
        table.setName(DSLName.of("dual"));
        SQLCommentStatement tableCommentStatement = DDLSQLSupport.createTableCommentStatement("test", table, DBType.POSTGRESQL);

        String sqlString = SQLUtils.toSQLString(tableCommentStatement);

        assertEquals("COMMENT ON TABLE PUBLIC.dual IS 'test'", sqlString);

    }

    @Test
    void testCreateCommentStatement() {

        ColumnDef columnDef =
                ColumnDef.builder()
                        .comment("测试")
                        .dslName(DSLName.of("test"))
                        .build();

        Table table = new Table();
        table.setName(DSLName.of("dual"));

        SQLCommentStatement commentStatement = DDLSQLSupport.createCommentStatement(columnDef, table, DBType.POSTGRESQL);


        String sqlString = SQLUtils.toSQLString(commentStatement);

        assertEquals("COMMENT ON COLUMN PUBLIC.dual.test IS '测试'", sqlString);
    }
}
