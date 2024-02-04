package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.executor.interceptor.*;
import cc.allio.uno.data.orm.dsl.DSLException;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 实现自{@link CommandExecutor}的基本的类，所有的具体实现类都需要继承该类。
 *
 * @author jiangwei
 * @date 2024/1/8 10:45
 * @since 1.1.6
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
     */
    protected abstract boolean doBool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler);

    @Override
    public <R> List<R> queryList(QueryOperator queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        return aspect(queryOperator, commandType, () -> doQueryList(queryOperator, commandType, resultSetHandler));
    }

    /**
     * 子类实现
     *
     * @param queryOperator    QueryOperator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    protected abstract <R> List<R> doQueryList(QueryOperator queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler);

    /**
     * 给定操作的切面并执行
     *
     * @param operator    operator
     * @param commandType commandType
     * @param operate     给定操作
     * @param <T>         操作返回类型
     * @return 返回类结果
     */
    private <T> T aspect(Operator<?> operator, CommandType commandType, Supplier<T> operate) {
        List<Interceptor> interceptors = options.getInterceptors();
        InterceptorAttributes beforeAttributes = new InterceptorAttributes(this, operator, commandType);
        var before = Mono.defer(() -> {
            // before interceptor
            List<BeforeInterceptor> beforeInterceptors = interceptors.stream().map(BeforeInterceptor::new).collect(Collectors.toList());
            InterceptorChainImpl beforeChain = new InterceptorChainImpl(beforeInterceptors);
            return beforeChain.proceed(new InterceptorChainContext(beforeAttributes));
        });
        var after = Mono.defer(() -> {
            Mono<T> result = Mono.fromSupplier(operate);
            List<AfterInterceptor> afterInterceptors = interceptors.stream().map(AfterInterceptor::new).collect(Collectors.toList());
            InterceptorAttributes afterAttributes = new InterceptorAttributes(beforeAttributes, result);
            InterceptorChainImpl afterChain = new InterceptorChainImpl(afterInterceptors);
            return afterChain.proceed(new InterceptorChainContext(afterAttributes)).then(result);
        });
        return before.then(after).block();
    }

}
