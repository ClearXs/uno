package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.interceptor.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.internal.SCOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.internal.STInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 实现自{@link CommandExecutor}的基本的类，所有的具体实现类都需要继承该类。
 *
 * @author j.x
 * @date 2024/1/8 10:45
 * @since 1.1.7
 */
@Slf4j
@Getter
public abstract class AbstractCommandExecutor implements CommandExecutor {

    private final ExecutorOptions options;

    protected AbstractCommandExecutor(ExecutorOptions options) {
        this.options = options;
    }

    @Override
    public boolean bool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        return aspect(operator, commandType, () -> doBool(operator, commandType, resultSetHandler));
    }

    /**
     * 子类实现
     *
     * @param operator         DSLOperator操作
     * @param commandType      DSL命令
     * @param resultSetHandler 结果集处理器
     * @return true 成功 false 失败
     * @throws DSLException invoke process has error
     */
    protected boolean doBool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        InnerCommandExecutorManager manager = getInnerCommandExecutorManager();
        if (manager == null) {
            throw new DSLException("inner command executor manager is null, can't execute operator");
        }
        try {
            switch (commandType) {
                case CREATE_TABLE:
                    return manager.getCreateTable().exec(operator, resultSetHandler);
                case DELETE_TABLE:
                    return manager.getDeleteTable().exec(operator, resultSetHandler);
                case EXIST_TABLE:
                    return manager.getExistTable().exec(operator, resultSetHandler);
                case ALERT_TABLE:
                    return manager.getAlter().exec(operator, resultSetHandler);
                case INSERT:
                    return manager.getInsert().exec(operator, resultSetHandler);
                case UPDATE:
                    return manager.getUpdate().exec(operator, resultSetHandler);
                case DELETE: {
                    // maybe execute logic delete
                    if (operator instanceof UpdateOperator) {
                        return manager.getUpdate().exec(operator, resultSetHandler);
                    } else {
                        return manager.getDelete().exec(operator, resultSetHandler);
                    }
                }
                default:
                    throw new DSLException(String.format("unknown command type in bool %s, accepted " +
                            "'CREATE_TABLE', 'DELETE_TABLE', 'EXIST_TABLE', 'ALERT_TABLE', 'INSERT', 'UPDATE', 'DELETE'", commandType));
            }
        } catch (Throwable ex) {
            throw new DSLException(String.format("exec operator %s has err", operator.getClass().getName()), ex);
        }
    }

    @Override
    public <R> List<R> queryList(Operator<?> queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        return aspect(queryOperator, commandType, () -> doQueryList(queryOperator, commandType, resultSetHandler));
    }

    /**
     * 子类实现
     *
     * @param <R>              返回结果类型
     * @param operator         operator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @return List
     * @throws DSLException query failed throw
     */
    protected <R> List<R> doQueryList(Operator<?> operator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        InnerCommandExecutorManager manager = getInnerCommandExecutorManager();
        if (manager == null) {
            throw new DSLException("inner command executor manager is null, can't execute operator");
        }

        try {
            switch (commandType) {
                case SHOW_TABLES:
                    return manager.<R, ShowTablesOperator, STInnerCommandExecutor<R, ShowTablesOperator>>getShowTable().exec(operator, resultSetHandler);
                case SHOW_COLUMNS:
                    return manager.<R, ShowColumnsOperator, SCOInnerCommandExecutor<R, ShowColumnsOperator>>getShowColumn().exec(operator, resultSetHandler);
                case SELECT:
                    return manager.<R, QueryOperator, QOInnerCommandExecutor<R, QueryOperator>>getQuery().exec(operator, resultSetHandler);
                default:
                    throw new DSLException(String.format("unknown command type in queryList %s, accepted 'SHOW_TABLES', 'SHOW_COLUMNS', 'SELECT'", commandType));
            }
        } catch (Throwable ex) {
            throw new DSLException("exec query list has err", ex);
        }
    }

    /**
     * 给定操作的切面并执行
     *
     * @param operator    operator
     * @param commandType commandType
     * @param operate     给定操作
     * @param <T>         操作返回类型
     * @return 返回类结果
     */
    <T> T aspect(Operator<?> operator, CommandType commandType, Supplier<T> operate) {
        List<Interceptor> interceptors = options.getInterceptors();
        InterceptorAttributes beforeAttributes = new InterceptorAttributes(this, operator, commandType);
        Mono<InterceptorAttributes> before = Mono.defer(() -> {
            // before interceptor
            List<BeforeInterceptor> beforeInterceptors = interceptors.stream().map(BeforeInterceptor::new).collect(Collectors.toList());
            InterceptorChainImpl beforeChain = new InterceptorChainImpl(beforeInterceptors);
            return beforeChain.proceed(new InterceptorChainContext(beforeAttributes));
        });
        Mono<T> after = Mono.defer(() -> {
            Mono<T> result = Mono.fromSupplier(operate);
            List<AfterInterceptor> afterInterceptors = interceptors.stream().map(AfterInterceptor::new).collect(Collectors.toList());
            InterceptorAttributes afterAttributes = new InterceptorAttributes(beforeAttributes, result);
            InterceptorChainImpl afterChain = new InterceptorChainImpl(afterInterceptors);
            return afterChain.proceed(new InterceptorChainContext(afterAttributes)).then(result);
        });
        return before.then(after).block();
    }

    /**
     * sub-class implementation, must be not null
     *
     * @return InnerCommandExecutorManager
     */
    protected abstract InnerCommandExecutorManager getInnerCommandExecutorManager();
}
