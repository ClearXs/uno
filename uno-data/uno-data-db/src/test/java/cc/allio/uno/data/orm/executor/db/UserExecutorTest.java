package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.test.model.User;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@RunTest
@MybatisEnv
public class UserExecutorTest extends BaseTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    DbCommandExecutor dbCommandExecutor;

    @BeforeEach
    void init() {
        dbCommandExecutor = new DbCommandExecutor(new DbMybatisConfiguration(sqlSessionFactory.getConfiguration()));
        dbCommandExecutor.getOptions().addInterceptor(new AuditInterceptor());
        dbCommandExecutor.getOptions().addInterceptor(new PrintInterceptor());
        dbCommandExecutor.getOptions().addInterceptor(new LogicInterceptor());
        dbCommandExecutor.createTable(User.class);
    }

    @Test
    void testBatchInsert() {
        ArrayList<User> users = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setName(String.valueOf(i));
            users.add(user);
        }
        dbCommandExecutor.batchInsertPojos(users);
        List<User> users1 = dbCommandExecutor.queryList(User.class);
        assertEquals(2, users1.size());
    }

    @Test
    void testInsertThenUpdate() {
        ArrayList<User> users = Lists.newArrayList();
        for (int i = 0; i < 1; i++) {
            User user = new User();
            user.setName(String.valueOf(i));
            users.add(user);
        }

        dbCommandExecutor.batchInsertPojos(users);

        User dbUser = dbCommandExecutor.queryOne(User.class);
        assertNotNull(dbUser.getName());

        User user = new User();
        dbCommandExecutor.updatePojo(user);

        dbUser = dbCommandExecutor.queryOne(User.class);
        assertNull(dbUser.getName());
    }

    @Test
    void testSaveOrUpdate() {
        User user = new User();
        user.setName("1");

        dbCommandExecutor.saveOrUpdate(user);

        user = dbCommandExecutor.queryOne(User.class);
        user.setName("2");
        dbCommandExecutor.saveOrUpdate(user);


        List<User> users = dbCommandExecutor.queryList(User.class);
        assertEquals(1, users.size());
        User dbUser = users.get(0);

        assertEquals("2", dbUser.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("1");

        dbCommandExecutor.saveOrUpdate(user);
        user = dbCommandExecutor.queryOne(User.class);
        dbCommandExecutor.delete(user);

        List<User> users = dbCommandExecutor.queryList(User.class);

        assertEquals(0, users.size());
    }

    @Test
    void testAlertTables() {
        dbCommandExecutor.alertTable(User.class, o -> o.rename("T_USERS_1"));
        Table table = dbCommandExecutor.showOneTable("T_USERS_1");
        assertNotNull(table);

        assertEquals("T_USERS_1", table.getName().format());
    }

    @AfterEach
    void destroy() {
        dbCommandExecutor.dropTable(User.class);
    }
}
