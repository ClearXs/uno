package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@RunTest
@MybatisEnv
public class DbCommandExecutorTest extends BaseTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    DbCommandExecutor mybatisCommandExecutor;

    @BeforeEach
    void init() {
        mybatisCommandExecutor = new DbCommandExecutor(new MybatisConfiguration(sqlSessionFactory.getConfiguration()));
        mybatisCommandExecutor.getOptions().addInterceptor(new PrintInterceptor());
        mybatisCommandExecutor.getOptions().addInterceptor(new NothingInterceptor());
        String sql = "CREATE TABLE dual (\n" +
                "\tname char(9) PRIMARY KEY NOT NULL UNIQUE\n" +
                ")";
        mybatisCommandExecutor.createTable(o -> o.parse(sql));
    }

    @Test
    void testConfigurationNotNull() {
        assertNotNull(mybatisCommandExecutor);
    }

    @Test
    void testShowTables() {
        List<Table> tables = mybatisCommandExecutor.showTables(o -> o);
        assertEquals(1, tables.size());
    }

    @Test
    void testShowColumns() {
        List<ColumnDef> columnDefs = mybatisCommandExecutor.showColumns(o -> o.from(DSLName.of("DUAL", DSLName.PLAIN_FEATURE)));
        assertEquals(1, columnDefs.size());
    }


    @AfterEach
    void destroy() {
        mybatisCommandExecutor.dropTable(DSLName.of("DUAL", DSLName.PLAIN_FEATURE));
    }
}
