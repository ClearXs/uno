package cc.allio.uno.data.orm;

import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchQueryOperator;
import cc.allio.uno.data.orm.executor.elasticsearch.EsSQLCommandExecutor;
import cc.allio.uno.data.orm.sql.SQLColumnDef;
import cc.allio.uno.test.BaseTestCase;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ElasticSearchSQLCommandExecutorTest extends BaseTestCase {

    ElasticSearchQueryOperator queryOperator = new ElasticSearchQueryOperator();
    EsSQLCommandExecutor sqlCommandExecutor;

    // ===========================  DML ===========================

    @BeforeEach
    @Override
    public void setup() {
        HttpHost httpHost = HttpHost.create("http://43.143.195.208:9200");
        RestClientBuilder builder = RestClient.builder(httpHost);
        sqlCommandExecutor = new EsSQLCommandExecutor(new Object[]{builder});
    }

    @Test
    void testInsertData() {
        User user = new User();
        user.setId(1L);
        user.setUserName("1");
        user.setPassword("2");
        boolean insert = sqlCommandExecutor.insertPojo(user);
        assertTrue(insert);
    }

    @Test
    void testQueryOne() {
        User user = sqlCommandExecutor.queryOneById(User.class, 1L);
        assertNotNull(user);
    }

    @Test
    void testUpdateData() {
        User user = new User();
        user.setUserName("aaa");
        user.setPassword("333");
        boolean update = sqlCommandExecutor.updatePojoById(user, 1L);
        assertTrue(update);

        User user1 = sqlCommandExecutor.queryOneById(User.class, 1L);
        System.out.println(user1);

    }

    @Test
    void testEQQuery() {
        List<User> users = sqlCommandExecutor.queryList(o -> o.from(User.class).eq(User::getUserName, "aaa"), User.class);
        System.out.println(users);
    }

    // ===========================  DDL ===========================

    @Test
    void testCreateIndex() {
        boolean isCreate = sqlCommandExecutor.createTable(User.class);
        assertTrue(isCreate);
    }

    @Test
    void testDropIndex() {
        boolean isDrop = sqlCommandExecutor.dropTable(User.class);
        assertTrue(isDrop);
    }

    @Test
    void testExistIndex() {
        boolean isExist = sqlCommandExecutor.existTable(User.class);
        assertFalse(isExist);
    }

    @Test
    void testShowColumns() {
        List<SQLColumnDef> sqlColumnDefs = sqlCommandExecutor.showColumns(User.class);
        assertEquals(3, sqlColumnDefs.size());
    }

}
