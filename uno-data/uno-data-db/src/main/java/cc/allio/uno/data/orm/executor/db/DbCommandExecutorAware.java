package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.executor.CommandExecutorAware;

/**
 * 获取{@link DbCommandExecutor}实例标识接口
 *
 * @author j.x
 * @since 1.1.7
 */
public interface DbCommandExecutorAware extends CommandExecutorAware<DbCommandExecutor> {
}
