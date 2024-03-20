package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.test.executor.TestCommandExecutorOptions;
import cc.allio.uno.test.testcontainers.Container;

public class TestTranslator implements ContainerExecutorOptionsTranslator {

    @Override
    public DBType withDBType(Container testContainer) {
        return TestCommandExecutorOptions.TEST_DB;
    }

    @Override
    public ExecutorKey withExecutorKey(Container testContainer) {
        return TestCommandExecutorOptions.TEST_EXECUTOR_KEY;
    }

    @Override
    public OperatorKey withOperatorKey(Container testContainer) {
        return TestCommandExecutorOptions.TEST_OPERATOR_KEY;
    }

    @Override
    public String withDatabase(Container testContainer) {
        return "test";
    }

    @Override
    public String withUsername(Container testContainer) {
        return "test";
    }

    @Override
    public String withPassword(Container testContainer) {
        return "test";
    }
}
