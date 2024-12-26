package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.executor.handler.*;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.lang.annotation.*;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.List;

/**
 * Command Executor
 *
 * @author j.x
 * @since 1.1.4
 */
public interface CommandExecutor {

    /**
     * 设置作为command executor key
     */
    String DEFAULT_KEY = "key";

    /**
     * 获取{@link ExecutorOptions#isSystemDefault()}时的默认key
     *
     * @return default key
     */
    static String getDefaultKey() {
        return Envs.getProperty(DEFAULT_KEY);
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator Operator操作
     * @param command  command
     * @return true 成功 false 失败
     */
    default boolean bool(Operator<?> operator, CommandType command) {
        return bool(operator, command, getOptions().obtainBoolResultHandler());
    }

    /**
     * bool操作，包含创建、更新、删除、插入表、删除表...
     *
     * @param operator         operator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @return true 成功 false 失败
     */
    boolean bool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler);

    /**
     * 查询list
     *
     * @param queryOperator    queryOperator
     * @param commandType      命令类型
     * @param resultSetHandler 结果集处理器
     * @param <R>              返回结果类型
     * @return List
     * @throws DSLException query failed throw
     */
    <R> List<R> queryList(Operator<?> queryOperator, CommandType commandType, ListResultSetHandler<R> resultSetHandler);

    /**
     * 检查给定的集合是否只有一个元素
     *
     * @param collection 集合
     * @param <T>        集合类型
     * @throws DSLException 当给定的集合大于一时抛出
     */
    default <T> T checkCollectionIsOneAndGet(Collection<T> collection) {
        if (CollectionUtils.isNotEmpty(collection)) {
            int size = collection.size();
            if (size == 1) {
                return collection.stream().findFirst().orElse(null);
            } else if (size > 1) {
                throw new DSLException("Expected one result (or null) to be returned by queryOne(), but found: " + size);
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 检查连接是否正常
     *
     * @return it ture normal connection
     * @throws SocketTimeoutException timeout throws
     */
    boolean check() throws SocketTimeoutException;

    /**
     * 获取执行器key
     *
     * @return the {@link ExecutorKey} instance
     */
    ExecutorKey getKey();

    /**
     * 获取DSL操作元数据。
     *
     * @return the {@link Operators} instance
     */
    Operators getOperatorGroup();

    /**
     * 获取执行器参数
     *
     * @return the {@link ExecutorOptions} instance
     */
    ExecutorOptions getOptions();

    /**
     * 销毁，关闭数据连接通道
     */
    void destroy();

    /**
     * Command Executor 分组注解
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Group {
        String value();
    }
}
