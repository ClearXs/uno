package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.data.orm.executor.CommandExecutorAware;

/**
 * an marked interface use for obtain {@link MongodbCommandExecutor}
 *
 * @author j.x
 * @date 2024/3/15 11:47
 * @since 1.1.7
 */
public interface MongodbCommandExecutorAware extends CommandExecutorAware<MongodbCommandExecutor> {
}
