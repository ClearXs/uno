package cc.allio.uno.data.orm;

import cc.allio.uno.data.orm.dialect.Dialect;
import cc.allio.uno.data.orm.dialect.H2Dialect;
import cc.allio.uno.data.orm.dialect.func.FuncDescriptor;
import cc.allio.uno.data.orm.dialect.func.ReuseFuncFactory;
import cc.allio.uno.data.orm.dialect.type.JdbcType;
import cc.allio.uno.data.sql.Alias;
import cc.allio.uno.data.sql.PlainColumn;
import cc.allio.uno.data.sql.word.Distinct;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.sql.Types;

class H2DialectTest extends BaseTestCase {

    Dialect dialect = new H2Dialect();

    @Test
    void testFindFunc() {
        FuncDescriptor func = dialect.getFuncRegistry().findFunc("count");
        assertEquals("count", func.getFuncName());
    }

    @Test
    void testCountFunc() {
        FuncDescriptor func = ReuseFuncFactory.count(dialect);
        String sql = func.render(new Object[]{new PlainColumn("user_name", null, null)});
        assertEquals("count(user_name)", sql);
        String sql2 = func.render(new Object[]{new PlainColumn("user_name", null, null), new Alias("b")});
        assertEquals("count(user_name) b", sql2);
        String sql3 = func.render(new Object[]{new Distinct(), new PlainColumn("user_name", null, null), new Alias("b")});
        assertEquals("count(distinct user_name) b", sql3);
    }

    /**
     * Test Case: 查找 integer type
     */
    @Test
    void testIntegerType() {
        JdbcType integer = dialect.getTypeRegistry().findJdbcType(Types.INTEGER);
        assertNotNull(integer);
    }

    @Test
    void testVarcharType() {
        JdbcType varchar = dialect.getTypeRegistry().findJdbcType(Types.VARCHAR);
        assertNotNull(varchar);
    }

}
