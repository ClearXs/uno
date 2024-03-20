package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.CommandExecutorAware;

/**
 * 获取{@link EsCommandExecutor}实例标识接口
 *
 * @author j.x
 * @date 2024/1/10 18:18
 * @since 1.1.7
 */
public interface EsCommandExecutorAware extends CommandExecutorAware<EsCommandExecutor> {
}
