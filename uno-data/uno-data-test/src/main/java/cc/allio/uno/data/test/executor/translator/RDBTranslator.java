package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.testcontainers.Container;

/**
 * default implementation translator get executor key and translator operator key
 *
 * @author j.x
 * @date 2024/3/20 01:11
 * @since 1.1.7
 */
public abstract class RDBTranslator implements ContainerExecutorOptionsTranslator {

    @Override
    public ExecutorKey withExecutorKey(Container testContainer) {
        return ExecutorKey.DB;
    }

    @Override
    public OperatorKey withOperatorKey(Container testContainer) {
        return OperatorKey.SQL;
    }

    @Override
    public boolean withDefault() {
        return true;
    }
}
