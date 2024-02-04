package cc.allio.uno.data.orm.executor;

import lombok.extern.slf4j.Slf4j;

/**
 * DSL Executor factory
 *
 * @author jiangwei
 * @date 2023/4/16 23:38
 * @since 1.1.4
 */
@Slf4j
public final class CommandExecutorFactory {

    private static CommandExecutorRegistry registry;

    private CommandExecutorFactory() {
    }

    /**
     * 注册{@link CommandExecutor}实例
     *
     * @param commandExecutor commandExecutor
     * @param <T>             T
     * @return command executor
     */
    public static <T extends CommandExecutor> T register(T commandExecutor) {
        return getRegistry().registerCommandExecutor(commandExecutor.getOptions(), () -> commandExecutor, true);
    }

    /**
     * 基于{@link ExecutorKey#getSystemExecutorKey()}获取的{@link CommandExecutor}实例
     *
     * @return DSLExecutor or null
     */
    public static <T extends CommandExecutor> T getDSLExecutor() {
        return getDSLExecutor(ExecutorKey.getSystemExecutorKey());
    }

    /**
     * 获取{@link CommandExecutor}实例
     *
     * @param executorKey 判断使用何种执行器key
     * @return DSLExecutor
     */
    public static <T extends CommandExecutor> T getDSLExecutor(ExecutorKey executorKey) {
        return getRegistry().getCommandExecutor(executorKey);
    }

    /**
     * 根据唯一标识获取{@link CommandExecutor}实例
     *
     * @param key key
     * @return DSLExecutor
     */
    public static <T extends CommandExecutor> T getDSLExecutor(String key) {
        return getRegistry().getCommandExecutor(key);
    }

    /**
     * 移除Command executor
     *
     * @param executorKey executorKey
     * @return if true removed
     * @see CommandExecutorRegistry#remove(ExecutorKey)
     */
    public static boolean remove(ExecutorKey executorKey) {
        return getRegistry().remove(executorKey);
    }

    /**
     * 移除Command executor
     *
     * @param key key
     * @return if true removed
     * @see CommandExecutorRegistry#remove(String)
     */
    public static boolean remove(String key) {
        return getRegistry().remove(key);
    }

    private static CommandExecutorRegistry getRegistry() {
        if (registry == null) {
            setRegistry(new CommandExecutorRegistryImpl());
        }
        return registry;
    }

    public static void setRegistry(CommandExecutorRegistry registry) {
        CommandExecutorFactory.registry = registry;
    }
}
