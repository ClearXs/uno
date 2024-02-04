package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;

import java.util.List;

/**
 * {@link CommandExecutor}实例加载器
 *
 * @author jiangwei
 * @date 2024/1/10 16:02
 * @since 1.1.6
 */
public interface ExecutorLoader {

    /**
     * 基于{@link Interceptor}创建{@link CommandExecutor}实例
     *
     * @param interceptors interceptors
     * @return CommandExecutor
     */
    CommandExecutor load(List<Interceptor> interceptors);

    /**
     * 基于{@link ExecutorOptions}创建{@link CommandExecutor}实例
     *
     * @param executorOptions executorOptions
     * @return CommandExecutor
     */
    CommandExecutor load(ExecutorOptions executorOptions);

    /**
     * 判断给定的{@link DBType}当前{@link ExecutorLoader}是否可以加载
     *
     * @param dbType dbType
     * @return if true loaded
     */
    boolean match(DBType dbType);
}

