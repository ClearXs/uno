package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.test.executor.CommandExecutorSetter;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.RunContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@RunTest
@RunContainer(ContainerType.Mongodb)
class MongodbCommandExecutorTest extends BaseTestCase implements CommandExecutorSetter<MongodbCommandExecutor> {

    private MongodbCommandExecutor executor;

    @BeforeEach
    void createDual() {
        boolean success = executor.createTable(o -> o.from("dual"));
        assertTrue(success);
    }

    @Test
    void testShowCollection() {
        List<Table> tables = executor.showTables();
        assertEquals(1, tables.size());
        assertEquals("dual", tables.get(0).getName().format());
    }

    @Test
    void testInsertDocument() {
        InsertOperator insertOperator = executor.getOperatorGroup().insert();
        insertOperator.insert("a", "a", "b", "b").from("dual");
        boolean success = executor.insert(insertOperator);

        assertTrue(success);
    }

    @Test
    void testUpdateDocument() {
        prepareValues();

        UpdateOperator updateOperator = executor.getOperatorGroup().update();
        updateOperator.from("dual").update("a", "c").eq("a", "a");
        boolean success = executor.update(updateOperator);
        assertTrue(success);
    }

    @Test
    void testRenameAndDropCollection() {
        AlterTableOperator alterTableOperator = executor.getOperatorGroup().alterTables();
        alterTableOperator.from("dual").rename("dual2");

        boolean success = executor.alertTable(alterTableOperator);
        assertTrue(success);

        boolean existed = executor.existTable("dual2");
        assertTrue(existed);

        success = executor.dropTable(o -> o.from("dual2"));
        assertTrue(success);

        // exist
        existed = executor.existTable("dual2");
        assertFalse(existed);
    }

    @Test
    void testQuery() {
        prepareValues();

        List<ResultGroup> resultGroups = executor.queryList(o -> o.from("dual"));
        assertEquals(1, resultGroups.size());
        Map<String, Object> map = resultGroups.get(0).toMap();
        assertEquals("a", map.get("a"));

    }

    void prepareValues() {
        InsertOperator insertOperator = executor.getOperatorGroup().insert();
        insertOperator.insert("a", "a", "b", "b").from("dual");
        executor.insert(insertOperator);
    }

    @AfterEach
    void deleteDual() {
        executor.dropTable(o -> o.from("dual"));
    }

    @Override
    public void setCommandExecutor(MongodbCommandExecutor executor) {
        this.executor = executor;
    }
}
