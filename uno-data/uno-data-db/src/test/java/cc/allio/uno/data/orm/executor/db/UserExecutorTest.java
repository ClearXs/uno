package cc.allio.uno.data.orm.executor.db;

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

    DbCommandExecutor mybatisCommandExecutor;

    @BeforeEach
    void init() {
        mybatisCommandExecutor = new DbCommandExecutor(new MybatisConfiguration(sqlSessionFactory.getConfiguration()));
        mybatisCommandExecutor.getOptions().addInterceptor(new AuditInterceptor());
        mybatisCommandExecutor.getOptions().addInterceptor(new PrintInterceptor());
        mybatisCommandExecutor.getOptions().addInterceptor(new LogicInterceptor());
        mybatisCommandExecutor.createTable(User.class);
    }

    @Test
    void testBatchInsert() {
        ArrayList<User> users = Lists.newArrayList();
        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setName(String.valueOf(i));
            users.add(user);
        }
        mybatisCommandExecutor.batchInsertPojos(users);
        List<User> users1 = mybatisCommandExecutor.queryList(User.class);
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

        mybatisCommandExecutor.batchInsertPojos(users);

        User dbUser = mybatisCommandExecutor.queryOne(User.class);
        assertNotNull(dbUser.getName());

        User user = new User();
        mybatisCommandExecutor.updatePojo(user);

        dbUser = mybatisCommandExecutor.queryOne(User.class);
        assertNull(dbUser.getName());
    }

    @Test
    void testSaveOrUpdate() {
        User user = new User();
        user.setName("1");

        mybatisCommandExecutor.saveOrUpdate(user);

        user = mybatisCommandExecutor.queryOne(User.class);
        user.setName("2");
        mybatisCommandExecutor.saveOrUpdate(user);


        List<User> users = mybatisCommandExecutor.queryList(User.class);
        assertEquals(1, users.size());
        User dbUser = users.get(0);

        assertEquals("2", dbUser.getName());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setName("1");

        mybatisCommandExecutor.saveOrUpdate(user);
        user = mybatisCommandExecutor.queryOne(User.class);
        mybatisCommandExecutor.delete(user);

        List<User> users = mybatisCommandExecutor.queryList(User.class);

        assertEquals(0, users.size());
    }


    @AfterEach
    void destroy() {
        mybatisCommandExecutor.dropTable(User.class);
    }
}
