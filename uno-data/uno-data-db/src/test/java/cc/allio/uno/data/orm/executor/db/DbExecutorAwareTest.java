package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.data.orm.config.db.DbAutoConfiguration;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ExecutorInitializerAutoConfiguration;
import cc.allio.uno.test.BaseTestCase;
import cc.allio.uno.test.Inject;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.env.annotation.MybatisEnv;
import org.junit.jupiter.api.Test;

@RunTest(components = {DbAutoConfiguration.class, ExecutorInitializerAutoConfiguration.class, DbExecutorAwareTest.UserRepository.class})
@MybatisEnv
class DbExecutorAwareTest extends BaseTestCase {

    @Inject
    private UserRepository userRepository;

    @Test
    void testBeAbleGetExecutor() {
        CommandExecutor executor = userRepository.getExecutor();
        boolean assignable = ClassUtils.isAssignable(DbCommandExecutor.class, executor.getClass());
        assertTrue(assignable);
    }


    static class UserRepository implements DbCommandExecutorAware {

    }
}
