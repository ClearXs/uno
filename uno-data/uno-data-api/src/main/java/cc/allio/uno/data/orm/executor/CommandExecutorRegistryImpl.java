package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * command executor registry
 *
 * @author jiangwei
 * @date 2024/1/25 17:07
 * @since 1.1.6
 */
@Slf4j
public class CommandExecutorRegistryImpl implements CommandExecutorRegistry {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    // 以唯一标识作为key的Map
    private final Map<String, CommandExecutor> commandExecutorMap = Maps.newConcurrentMap();
    // 以option作为key，获取commandExecutor的的唯一标识
    // option的equals是根据 DBType ExecutorKey OperatorKey
    private final Map<ExecutorOptions, String> optionsKeyMap = Maps.newConcurrentMap();

    private final List<ExecutorLoader> loaders;

    public CommandExecutorRegistryImpl() {
        this(Collections.emptyList());
    }

    public CommandExecutorRegistryImpl(List<ExecutorLoader> loaders) {
        this.loaders = loaders;
    }

    @Override
    public <T extends CommandExecutor> T crate(@NotNull ExecutorOptions executorOptions) {
        DBType dbType = executorOptions.getDbType();
        ExecutorLoader executorLoader =
                loaders.stream()
                        .filter(loader -> loader.match(dbType))
                        .findFirst()
                        .orElse(null);
        if (executorLoader == null) {
            return null;
        }
        return (T) executorLoader.load(executorOptions);
    }

    @Override
    public <T extends CommandExecutor> T createAndRegister(@NotNull ExecutorOptions executorOptions) {
        DBType dbType = executorOptions.getDbType();
        ExecutorLoader executorLoader =
                loaders.stream()
                        .filter(loader -> loader.match(dbType))
                        .findFirst()
                        .orElse(null);
        if (executorLoader == null) {
            return null;
        }
        return (T) registerCommandExecutor(executorOptions, () -> executorLoader.load(executorOptions), true);
    }

    @Override
    public <T extends CommandExecutor> T registerCommandExecutor(ExecutorOptions executorOptions, Supplier<T> commandExecutorSupplier, boolean ifPresent) {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            String key = executorOptions.getKey();
            if (log.isDebugEnabled()) {
                log.debug("register command executor key: {}, executorOptions: {}", key, executorOptions.getKey());
            }
            T commandExecutor;
            if (ifPresent) {
                commandExecutor = (T) commandExecutorMap.compute(key, (k, v) -> commandExecutorSupplier.get());
                optionsKeyMap.compute(executorOptions, (k, v) -> key);
            } else {
                commandExecutor = (T) commandExecutorMap.computeIfAbsent(key, k -> commandExecutorSupplier.get());
                optionsKeyMap.computeIfAbsent(executorOptions, k -> key);
            }
            boolean systemDefault = executorOptions.isSystemDefault();
            if (systemDefault) {
                Envs.setProperty(ExecutorKey.DSL_EXECUTOR_TYPE_KEY, executorOptions.getExecutorKey().key());
                Envs.setProperty(CommandExecutor.DEFAULT_KEY, key);
            }
            return commandExecutor;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public <T extends CommandExecutor> T getCommandExecutor(ExecutorKey executorKey) {
        if (executorKey == null) {
            return null;
        }
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            ExecutorOptions options = match(executorKey);
            if (options == null) {
                return null;
            }
            String key = optionsKeyMap.get(options);
            if (StringUtils.isBlank(key)) {
                return null;
            }
            return getCommandExecutor(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public <T extends CommandExecutor> T getCommandExecutor(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            return (T) commandExecutorMap.get(key);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean remove(ExecutorKey executorKey) {
        List<ExecutorOptions> executorOptions = matchAll(executorKey);
        if (CollectionUtils.isNotEmpty(executorOptions)) {
            return executorOptions.stream()
                    .allMatch(options -> {
                        String key = optionsKeyMap.get(options);
                        return remove(key);
                    });
        }
        return false;
    }

    @Override
    public boolean remove(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            CommandExecutor commandExecutor = commandExecutorMap.get(key);
            if (commandExecutor == null) {
                return false;
            }
            ExecutorOptions options = commandExecutor.getOptions();
            if (log.isDebugEnabled()) {
                log.debug("remove command executor {}", options);
            }
            try {
                commandExecutor.destroy();
            } catch (Throwable ex) {
                log.error("command executor destroy has error, the command options is {}", options, ex);
            }
            commandExecutorMap.remove(key);
            optionsKeyMap.remove(options);
            return true;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean has(String key) {
        if (StringUtils.isBlank(key)) {
            return false;
        }
        return commandExecutorMap.containsKey(key);
    }

    @Override
    public boolean has(ExecutorKey executorKey) {
        ExecutorOptions executorOptions = match(executorKey);
        return executorOptions != null;
    }

    @Override
    public List<CommandExecutor> getAllDefault() {
        return commandExecutorMap.values()
                .stream()
                .filter(commandExecutor -> commandExecutor.getOptions().isSystemDefault())
                .toList();
    }

    @Override
    public List<CommandExecutor> getAll() {
        return Lists.newArrayList(commandExecutorMap.values());
    }

    @Override
    public void clear() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Collection<CommandExecutor> values = commandExecutorMap.values();
            for (CommandExecutor executor : values) {
                remove(executor.getOptions().getKey());
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 根据executor key最佳匹配某一个{@link ExecutorOptions}
     *
     * @param executorKey executorKey
     * @return ExecutorOptions or null
     */
    private ExecutorOptions match(ExecutorKey executorKey) {
        Set<ExecutorOptions> executorOptions = optionsKeyMap.keySet();
        for (ExecutorOptions executorOption : executorOptions) {
            // 基于 key 进行比较
            if (executorOption.getExecutorKey().equals(executorKey)) {
                return executorOption;
            }
        }
        return null;
    }

    /**
     * 匹配所有包含executorKey的{@link ExecutorOptions}
     *
     * @param executorKey executorKey
     * @return list
     */
    private List<ExecutorOptions> matchAll(ExecutorKey executorKey) {
        return optionsKeyMap.keySet()
                .stream()
                .filter(options -> options.getExecutorKey().equals(executorKey))
                .toList();
    }
}
