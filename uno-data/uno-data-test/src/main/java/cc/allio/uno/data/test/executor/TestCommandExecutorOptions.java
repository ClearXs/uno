package cc.allio.uno.data.test.executor;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;

public interface TestCommandExecutorOptions {

    DBType TEST_DB = new DBType.DefaultDBType("test", "", null, "");
    ExecutorKey TEST_EXECUTOR_KEY = ExecutorKey.returnKey("test");
    OperatorKey TEST_OPERATOR_KEY = OperatorKey.returnKey("test");
}
