package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.ClassUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * SQL Executor simple Factory
 *
 * @author jiangwei
 * @date 2023/4/16 23:38
 * @since 1.1.4
 */
@Slf4j
public final class CommandExecutorFactory {

    private static final Map<ExecutorKey, CommandExecutor> CACHES = Maps.newConcurrentMap();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private CommandExecutorFactory() {
    }

    public static <T extends CommandExecutor> T register(ExecutorKey executorKey, Class<T> executorClass, Object... values) {
        CommandExecutor executor = CACHES.computeIfAbsent(executorKey, k -> ClassUtils.newInstance(executorClass, values));
        return register(executor);
    }

    /**
     * 注册{@link CommandExecutor}实例
     *
     * @param commandExecutor commandExecutor
     * @param <T>             T
     * @return command executor
     */
    public static <T extends CommandExecutor> T register(CommandExecutor commandExecutor) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            CommandExecutor executor = CACHES.computeIfAbsent(commandExecutor.getKey(), k -> commandExecutor);
            if (log.isDebugEnabled()) {
                log.debug("Command executor register instance executor [{}]", executor.getClass().getName());
            }
            Envs.setProperty(ExecutorKey.SQL_EXECUTOR_TYPE_KEY, commandExecutor.getKey().key());
            return (T) executor;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 获取{@link CommandExecutor}实例
     *
     * @return SQLExecutor
     */
    public static <T extends CommandExecutor> T getSQLExecutor() {
        return getSQLExecutor(ExecutorKey.getSystemExecutorKey());
    }

    /**
     * 获取{@link CommandExecutor}实例
     *
     * @param executorKey 判断使用何种执行器key
     * @return SQLExecutor
     */
    public static <T extends CommandExecutor> T getSQLExecutor(ExecutorKey executorKey) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return (T) Optional.ofNullable(executorKey).map(CACHES::get).orElse(null);
        } finally {
            readLock.unlock();
        }
    }
}
