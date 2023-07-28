package cc.allio.uno.data.orm.sql.dml.druid;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import cc.allio.uno.data.orm.type.DBType;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

public class DruidSelectTest extends BaseTestCase {

    @Test
    void testSelect() {
        DruidSelect druidSelect = new DruidSelect(DBType.H2);
        String sql = druidSelect.select("z").getSQL();
        assertEquals("z", sql);
    }

    @Test
    void testAggregate() {
        DruidSelect druidSelect = new DruidSelect(DBType.H2);
        String count = druidSelect.count().getSQL();
        assertEquals("count(*) AS count", count);

        String zCount = druidSelect.count("z", "zcount").getSQL();
        System.out.println(zCount);

    }

    @Test
    void testSQLParser() {
        SQLStatementParser sqlStatementParser = SQLParserUtils.createSQLStatementParser("", DbType.db2);

        SQLCreateTableStatement sqlCreateTableStatement = new SQLCreateTableStatement();
        sqlCreateTableStatement.setTableName("test");

        String sqlString = SQLUtils.toSQLString(sqlCreateTableStatement);

    }

}
