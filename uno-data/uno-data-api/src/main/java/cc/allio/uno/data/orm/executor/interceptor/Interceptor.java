package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.chain.Chain;
import cc.allio.uno.core.chain.ChainContext;
import cc.allio.uno.core.chain.Node;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import reactor.core.publisher.Mono;

/**
 * 允许用户在执行过程中添加自定义代码，参考自 hibernate #Interceptor
 *
 * @author j.x
 * @since 1.1.7
 */
public interface Interceptor extends Node<InterceptorAttributes, InterceptorAttributes> {

    @Override
    default Mono<InterceptorAttributes> execute(Chain<InterceptorAttributes, InterceptorAttributes> chain, ChainContext<InterceptorAttributes> context) throws Throwable {
        return chain.proceed(context);
    }

    /**
     * 在保存之前的回调，允许用户动态更改插入内容
     * <p>关联的方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#insert(InsertOperator)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    default void onSaveBefore(CommandExecutor commandExecutor, Operator<?> operator) {
    }

    /**
     * 在保存之后的回调
     * <p>关联的方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#insert(InsertOperator)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     * @param result          执行结果
     */
    default void onSaveAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
    }

    /**
     * 在更新之前回调，允许用户动态更改更新内容
     * <p>关联方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#update(UpdateOperator)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    default void onUpdateBefore(CommandExecutor commandExecutor, Operator<?> operator) {
    }

    /**
     * 在更新之后的回调
     * <p>关联的方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#update(UpdateOperator)} </p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     * @param result          执行结果
     */
    default void onUpdateAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
    }

    /**
     * 在删除之前回掉，允许用户动态更改删除内容
     * <p>关联方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#delete(DeleteOperator)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    default void onDeleteBefore(CommandExecutor commandExecutor, Operator<?> operator) {
    }

    /**
     * 在删除之后的回调
     * <p>关联的方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#delete(DeleteOperator)}L</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     * @param result          执行结果
     */
    default void onDeleteAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
    }

    /**
     * 在查询之前的回掉，允许用户动态更改查询内容
     * <p>关联方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#queryList(QueryOperator)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     */
    default void onQueryBefore(CommandExecutor commandExecutor, Operator<?> operator) {
    }

    /**
     * 在查询之后的回调
     * <p>关联的方法集是{@link cc.allio.uno.data.orm.executor.CommandExecutor#queryList(QueryOperator, CommandType, ListResultSetHandler)}</p>
     *
     * @param commandExecutor commandExecutor
     * @param operator        operator
     * @param result          执行结果
     */
    default void onQueryAfter(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
    }

    /**
     * 执行不明确命令的回调
     *
     * @param commandExecutor commandExecutor
     * @param commandType     commandType
     * @param operator        operator
     */
    default void onUnknownCommand(CommandExecutor commandExecutor, CommandType commandType, Operator<?> operator) {
    }
}
