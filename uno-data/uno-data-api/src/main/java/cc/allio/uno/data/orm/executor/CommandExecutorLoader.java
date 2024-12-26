package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.util.List;

/**
 * {@link CommandExecutor}实例加载器
 *
 * @author j.x
 * @since 1.1.7
 */
public interface CommandExecutorLoader<E extends AggregateCommandExecutor> {

    /**
     * 基于{@link Interceptor}创建{@link CommandExecutor}实例
     *
     * @param interceptors interceptors
     * @return CommandExecutor
     */
    E load(List<Interceptor> interceptors);

    /**
     * 基于{@link ExecutorOptions}创建{@link CommandExecutor}实例
     *
     * @param executorOptions executorOptions
     * @return CommandExecutor
     */
    E load(ExecutorOptions executorOptions);

    /**
     * 判断给定的{@link DBType}当前{@link CommandExecutorLoader}是否可以加载
     *
     * @param dbType dbType
     * @return if true loaded
     */
    boolean match(DBType dbType);
}

