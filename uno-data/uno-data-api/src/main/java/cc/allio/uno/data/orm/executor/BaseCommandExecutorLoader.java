package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.util.List;

/**
 * ingest general method for public base class on {@link CommandExecutorLoader}
 * <p>
 * for example determine {@link ExecutorOptions#isSystemDefault()} is true, if true then set db type as for default system env key
 * </p>
 * <p>
 * all relevant {@link CommandExecutorLoader} subclasses should be extended
 * </p>
 *
 * @author j.x
 * @since 1.1.8
 */
public abstract class BaseCommandExecutorLoader<E extends AggregateCommandExecutor> implements CommandExecutorLoader<E> {

    @Override
    public E load(List<Interceptor> interceptors) {
        return onLoad(interceptors);
    }

    @Override
    public E load(ExecutorOptions executorOptions) {
        if (executorOptions == null) {
            throw Exceptions.unNull("ExecutorOptions");
        }
        boolean systemDefault = executorOptions.isSystemDefault();
        if (systemDefault) {
            DBType dbType = executorOptions.getDbType();
            Envs.setProperty(DBType.DB_TYPE_CONFIG_KEY, dbType.getName());
        }
        return onLoad(executorOptions);
    }

    /**
     * sub-class implementation, base on {@link Interceptor} create {@link AggregateCommandExecutor} sub-class-instance
     *
     * @param interceptors the list of interceptors
     * @return {@link AggregateCommandExecutor} instance
     */
    protected abstract E onLoad(List<Interceptor> interceptors);

    /**
     * sub-class implementation, base on {@link ExecutorOptions} create {@link AggregateCommandExecutor} sub-class-instance
     *
     * @param executorOptions the {@link ExecutorOptions}
     * @return {@link AggregateCommandExecutor} instance
     */
    protected abstract E onLoad(ExecutorOptions executorOptions);
}
