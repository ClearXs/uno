package cc.allio.uno.data.orm;

import cc.allio.uno.data.orm.config.DataBaseAutoConfiguration;
import cc.allio.uno.data.orm.executor.mybatis.MybatisSQLCommandExecutor;
import cc.allio.uno.data.orm.sql.DruidOperatorMetadata;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperatorFactory;
import cc.allio.uno.data.orm.sql.ddl.SQLCreateTableOperator;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.DataSourceEnv;
import cc.allio.uno.test.env.annotation.MybatisPlusEnv;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.ibatis.session.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.sql.SQLException;
import java.util.List;

@RunTest(components = DataBaseAutoConfiguration.class)
@MybatisPlusEnv
@DataSourceEnv
public class MybatisSqlExecutorTest extends BaseTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Inject
    private MybatisSQLCommandExecutor sqlExecutor;

    @Inject
    private DruidOperatorMetadata druidOperatorMetadata;

    @Test
    void testCreate() throws SQLException {
        testData(sqlSessionFactory.getConfiguration());
    }

    @Test
    void testCreateByLambda() {
        sqlExecutor.createTable(f -> f.from("dual").column(SQLColumnDef.builder().sqlName(SQLName.of("t1")).build()));
    }

    @Test
    void testCreatePojo() {
        sqlExecutor.createTable(Demo.class);
    }

    @Test
    void testDropTable() {
        // 调用
        sqlExecutor.dropTable("dual");
        // lambda
        sqlExecutor.dropTable(f -> f.from("dula"));
        // pojo
        sqlExecutor.dropTable(Demo.class);
    }

    @Test
    void testExistTable() {
        // 调用
        sqlExecutor.existTable("dual");
        // lambda
        sqlExecutor.existTable(f -> f.from("dual"));
        // pojo
        sqlExecutor.existTable(Demo.class);
    }

    @Test
    void testInsert() {
        // 调用
        sqlExecutor.insert(f -> f.from("dual").insert("t1", "t1"));
        // pojo
        Demo demo = new Demo();
        sqlExecutor.insertPojo(demo);
        // batch
        sqlExecutor.batchInsertPojos(Lists.newArrayList(demo));
    }

    @Test
    void testUpdate() {
        // 调用
        sqlExecutor.update(f -> f.from("dual").update("t1", "t1"));
        // pojo where
        Demo demo = new Demo();
        sqlExecutor.updatePojoByCondition(demo, c -> c.eq("t1", "t1"));
    }

    @Test
    void testQuery() throws SQLException {
        testData(sqlSessionFactory.getConfiguration());
        User user = sqlExecutor.queryOne(f -> f.select("name").from("t_user").eq(User::getName, "1"), User.class);
        Assertions.assertEquals("1", user.name);
    }

    @Test
    void testShowColumns() {
        testData(sqlSessionFactory.getConfiguration());
        List<SQLColumnDef> users = sqlExecutor.showColumns(SQLOperatorFactory.getSQLOperator(SQLShowColumnsOperator.class).from("t_user"));
        assertEquals(1, users.size());
    }

    void testData(Configuration configuration) {
        sqlExecutor.createTable(
                SQLOperatorFactory.getSQLOperator(SQLCreateTableOperator.class)
                        .from("t_user")
                        .column(SQLColumnDef.builder().sqlName(SQLName.of("name")).build()));
        sqlExecutor.insert(SQLOperatorFactory.getSQLOperator(SQLInsertOperator.class).from("t_user").insert("name", "1"));
    }

    @Data
    public static class User {
        private String name;
    }

    @Data
    @Table(name = "dual")
    public static class Demo {

        @Column(name = "id")
        private String id;

        @Column(name = "name")
        private String name;
    }
}
