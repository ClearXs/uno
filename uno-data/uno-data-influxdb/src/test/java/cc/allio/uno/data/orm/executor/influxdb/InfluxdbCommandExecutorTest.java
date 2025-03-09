package cc.allio.uno.data.orm.executor.influxdb;

import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbDeleteOperator;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbInsertOperator;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.test.executor.CommandExecutorSetter;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.RunContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

@RunTest
@RunContainer(ContainerType.Influxdb)
public class InfluxdbCommandExecutorTest extends BaseTestCase implements CommandExecutorSetter<InfluxdbCommandExecutor> {

    InfluxdbCommandExecutor executor;

    @BeforeEach
    void init() {
        boolean dual = executor.createTable(o -> o.from("dual"));
        assertTrue(dual);
    }

    @Test
    void testShowBuckets() {
        List<Table> buckets = executor.showTables();
        // test-bucket
        // _monitoring
        // dual
        // _tasks
        assertEquals(4, buckets.size());
    }

    @Test
    void testDropBucket() {
        boolean dropped = executor.dropTable("dual");
        assertTrue(dropped);
        List<Table> buckets = executor.showTables();
        assertEquals(3, buckets.size());
    }

    @Test
    void testExistBucket() {
        boolean existed = executor.existTable("dual");
        assertTrue(existed);
    }

    @Test
    void testInsertTimeSeries() {
        boolean insert = executor.insert(
                o -> o.insert("a", 1, "b", 2)
                        .from("dual")
                        .castReality(InfluxdbInsertOperator.class)
                        .tags("t", "t"));
        assertTrue(insert);
    }

    @Test
    void testQueryTimeSeries() {
        prepareData();

        List<ResultGroup> resultGroups = executor.queryList(o -> o.selectAll().from("dual").eq("a", 1));
        assertEquals(2, resultGroups.size());
        resultGroups = executor.queryList(o -> o.selectAll().from("dual").eq("a", 2));
        assertEquals(0, resultGroups.size());

        // test count
        ResultGroup r = executor.queryOne(o -> o.from("dual").count().selectAll());
        assertEquals(2, r.getLongValue(BoolResultHandler.GUESS_COUNT));
    }


    @Test
    void testDeleteTimeSeries() {
        prepareData();

        boolean deleted = executor.delete(o -> o.from("dual").castReality(InfluxdbDeleteOperator.class));
        assertTrue(deleted);
        ResultGroup r = executor.queryOne(o -> o.from("dual").count().selectAll());
        assertEquals(0, r.getLongValue(BoolResultHandler.GUESS_COUNT));
    }

    void prepareData() {
        executor.insert(
                o -> o.insert("a", 1, "b", 2)
                        .from("dual")
                        .castReality(InfluxdbInsertOperator.class)
                        .tags("t", "t"));

        executor.insert(
                o -> o.insert("a", 1, "b", 2)
                        .from("dual")
                        .castReality(InfluxdbInsertOperator.class)
                        .tags("t", "t"));
    }

    @Test

    @Override
    public void setCommandExecutor(InfluxdbCommandExecutor executor) {
        this.executor = executor;
    }
}
