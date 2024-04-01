package cc.allio.uno.data.orm.executor;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * command executor registry，参考自{@link org.springframework.beans.factory.BeanFactory}
 *
 * @author j.x
 * @date 2024/1/29 13:04
 * @since 1.1.7
 */
public interface CommandExecutorRegistry {

    /**
     * 基于给定的{@link ExecutorOptions}从{@link CommandExecutorLoader}
     *
     * @param executorOptions executorOptions
     * @param <T>             command executor type
     * @return AggregateCommandExecutor instance or null
     */
    <T extends AggregateCommandExecutor> T crate(@NotNull ExecutorOptions executorOptions);

    /**
     * 基于给定的{@link ExecutorOptions}从{@link CommandExecutorLoader}中创建{@link AggregateCommandExecutor}并进行注册
     *
     * @param executorOptions executorOptions
     * @param <T>             command executor type
     * @return AggregateCommandExecutor instance or null
     */
    <T extends AggregateCommandExecutor> T createAndRegister(@NotNull ExecutorOptions executorOptions);

    /**
     * 注册 command executor，如果存在则不在进行设置
     *
     * @param executorOptions         executorOptions
     * @param commandExecutorSupplier commandExecutorSupplier
     * @param ifPresent               if true 如果已经存在则进行覆盖 否则不进行覆盖
     */
    <T extends AggregateCommandExecutor> T registerCommandExecutor(ExecutorOptions executorOptions, Supplier<T> commandExecutorSupplier, boolean ifPresent);

    /**
     * 基于最佳匹配原则获取最可能存在的{@link ExecutorOptions}，在基于此获取{@link AggregateCommandExecutor}实例
     *
     * @param executorKey executorKey
     * @return AggregateCommandExecutor or null
     */
    <T extends AggregateCommandExecutor> T getCommandExecutor(ExecutorKey executorKey);

    /**
     * 获取{@link AggregateCommandExecutor}实例
     *
     * @param key key
     * @return AggregateCommandExecutor
     */
    <T extends AggregateCommandExecutor> T getCommandExecutor(String key);

    /**
     * obtain {@link AggregateCommandExecutor} by {@link DBType}.
     * <p>if a number of {@link AggregateCommandExecutor}, then find first one</p>
     *
     * @param dbType the dbType
     * @param <T>    the {@link AggregateCommandExecutor} type
     * @return {@link AggregateCommandExecutor} instance or null
     */
    <T extends AggregateCommandExecutor> T getCommandExecutorByDBTypeFirst(DBType dbType);

    /**
     * 基于{@link ExecutorKey}批量移除找到{@link AggregateCommandExecutor}实例
     *
     * @param executorKey executorKey
     * @return if ture removed
     */
    boolean remove(ExecutorKey executorKey);

    /**
     * 基于唯一标识移除{@link AggregateCommandExecutor}实例
     *
     * @param key key
     * @return if ture removed
     */
    boolean remove(String key);

    /**
     * 根据给定的key判断是否有{@link AggregateCommandExecutor}存在
     *
     * @param key key
     * @return if true life
     */
    boolean has(String key);

    /**
     * 根据给定的{@link ExecutorKey}判断是否有{@link AggregateCommandExecutor}存在
     *
     * @param executorKey executorKey
     * @return if true life
     */
    boolean has(ExecutorKey executorKey);

    /**
     * 获取所有默认的{@link AggregateCommandExecutor}
     *
     * @return all default or empty list
     */
    List<AggregateCommandExecutor> getAllDefault();

    /**
     * 获取所有的CommandExecutor
     *
     * @return all or empty list
     */
    List<AggregateCommandExecutor> getAll();

    /**
     * 情空当前注册表里面所有的内容
     */
    void clear();
}
