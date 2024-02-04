package cc.allio.uno.data.orm.executor;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * command executor registry，参考自{@link org.springframework.beans.factory.BeanFactory}
 *
 * @author jiangwei
 * @date 2024/1/29 13:04
 * @since 1.1.6
 */
public interface CommandExecutorRegistry {

    /**
     * 基于给定的{@link ExecutorOptions}从{@link ExecutorLoader}
     *
     * @param executorOptions executorOptions
     * @param <T>             command executor type
     * @return CommandExecutor instance or null
     */
    <T extends CommandExecutor> T crate(@NotNull ExecutorOptions executorOptions);

    /**
     * 基于给定的{@link ExecutorOptions}从{@link ExecutorLoader}中创建{@link CommandExecutor}并进行注册
     *
     * @param executorOptions executorOptions
     * @param <T>             command executor type
     * @return CommandExecutor instance or null
     */
    <T extends CommandExecutor> T createAndRegister(@NotNull ExecutorOptions executorOptions);

    /**
     * 注册 command executor，如果存在则不在进行设置
     *
     * @param executorOptions         executorOptions
     * @param commandExecutorSupplier commandExecutorSupplier
     * @param ifPresent               if true 如果已经存在则进行覆盖 否则不进行覆盖
     */
    <T extends CommandExecutor> T registerCommandExecutor(ExecutorOptions executorOptions, Supplier<T> commandExecutorSupplier, boolean ifPresent);

    /**
     * 基于最佳匹配原则获取最可能存在的{@link ExecutorOptions}，在基于此获取{@link CommandExecutor}实例
     *
     * @param executorKey executorKey
     * @return CommandExecutor or null
     */
    <T extends CommandExecutor> T getCommandExecutor(ExecutorKey executorKey);

    /**
     * 获取{@link CommandExecutor}实例
     *
     * @param key key
     * @return CommandExecutor
     */
    <T extends CommandExecutor> T getCommandExecutor(String key);

    /**
     * 基于{@link ExecutorKey}批量移除找到{@link CommandExecutor}实例
     *
     * @param executorKey executorKey
     * @return if ture removed
     */
    boolean remove(ExecutorKey executorKey);

    /**
     * 基于唯一标识移除{@link CommandExecutor}实例
     *
     * @param key key
     * @return if ture removed
     */
    boolean remove(String key);

    /**
     * 根据给定的key判断是否有{@link CommandExecutor}存在
     *
     * @param key key
     * @return if true life
     */
    boolean has(String key);

    /**
     * 根据给定的{@link ExecutorKey}判断是否有{@link CommandExecutor}存在
     *
     * @param executorKey executorKey
     * @return if true life
     */
    boolean has(ExecutorKey executorKey);

    /**
     * 获取所有默认的{@link CommandExecutor}
     *
     * @return all default or empty list
     */
    List<CommandExecutor> getAllDefault();

    /**
     * 获取所有的CommandExecutor
     *
     * @return all or empty list
     */
    List<CommandExecutor> getAll();

    /**
     * 情空当前注册表里面所有的内容
     */
    void clear();
}
