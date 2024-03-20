package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.testcontainers.Container;

/**
 * mongodb impl translator
 *
 * @author j.x
 * @date 2024/3/20 01:19
 * @since 1.1.7
 */
public class MongodbTranslator implements ContainerExecutorOptionsTranslator {

    @Override
    public DBType withDBType(Container testContainer) {
        return DBType.MONGODB;
    }

    @Override
    public ExecutorKey withExecutorKey(Container testContainer) {
        return ExecutorKey.MONGODB;
    }

    @Override
    public OperatorKey withOperatorKey(Container testContainer) {
        return OperatorKey.MONGODB;
    }

    @Override
    public String withDatabase(Container testContainer) {
        return "test";
    }

    @Override
    public String withUsername(Container testContainer) {
        return StringPool.EMPTY;
    }

    @Override
    public String withPassword(Container testContainer) {
        return StringPool.EMPTY;
    }
}
