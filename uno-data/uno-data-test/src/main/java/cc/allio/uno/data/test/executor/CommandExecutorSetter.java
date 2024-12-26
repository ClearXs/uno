package cc.allio.uno.data.test.executor;

import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.test.testcontainers.RunContainer;

/**
 * Declaration a {@link CommandExecutor} set interface.
 * <p>This interface only one set method, when test class exits {@link RunContainer} annotation will trigger that method.</p>
 *
 * @author j.x
 * @since 0.1.1
 */
public interface CommandExecutorSetter<E extends AggregateCommandExecutor> {

    /**
     * set command executor
     *
     * @param executor executor
     */
    void setCommandExecutor(E executor);
}
