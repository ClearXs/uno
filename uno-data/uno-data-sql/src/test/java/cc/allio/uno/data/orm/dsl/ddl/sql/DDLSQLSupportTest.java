package cc.allio.uno.data.orm.dsl.ddl.sql;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.sql.ddl.DDLSQLSupport;
import cc.allio.uno.test.BaseTestCase;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLCommentStatement;
import org.junit.jupiter.api.Test;

public class DDLSQLSupportTest extends BaseTestCase {

    @Test
    void testCreateCommentStatement() {

        ColumnDef columnDef =
                ColumnDef.builder()
                        .comment("测试")
                        .dslName(DSLName.of("test"))
                        .build();

        Table table = new Table();
        table.setName(DSLName.of("dual"));

        SQLCommentStatement commentStatement = DDLSQLSupport.createCommentStatement(columnDef, table, DbType.postgresql);


        String sqlString = SQLUtils.toSQLString(commentStatement);

        assertEquals("COMMENT ON COLUMN PUBLIC.dual.test IS '测试'", sqlString);
    }
}
