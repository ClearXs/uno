package cc.allio.uno.data.test.executor;

import cc.allio.uno.core.spi.ClassPathServiceLoader;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutorLoader;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import cc.allio.uno.data.test.executor.translator.*;
import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.runner.RefreshCompleteRunner;
import cc.allio.uno.test.runner.Runner;
import cc.allio.uno.test.testcontainers.ContainerType;
import cc.allio.uno.test.testcontainers.Container;
import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * pick up test container arguments and transfer {@link ExecutorOptions}.
 * <p>and set {@link CommandExecutor} if test class implementation {@link CommandExecutorSetter}</p>
 *
 * @author j.x
 * @date 2024/3/20 00:44
 * @since 1.1.7
 */
@Slf4j
@AutoService(Runner.class)
@Priority(Integer.MAX_VALUE)
public class CommandExecutorInjectRunner implements RefreshCompleteRunner {

    private final Map<ContainerType, ContainerExecutorOptionsTranslator> translatorMap;
    private final List<CommandExecutorLoader> commandExecutorLoaders;

    public CommandExecutorInjectRunner() {
        this.translatorMap = Maps.newHashMap();
        this.translatorMap.put(ContainerType.MySQL, new MySQLTranslator());
        this.translatorMap.put(ContainerType.MSSQL, new MSSQLTranslator());
        this.translatorMap.put(ContainerType.PostgreSQL, new PostgreSQLTranslator());
        this.translatorMap.put(ContainerType.Mongodb, new MongodbTranslator());
        this.translatorMap.put(ContainerType.Influxdb, new InfluxdbTranslator());
        this.translatorMap.put(ContainerType.Test, new TestTranslator());
        // load command executor loader instance
        var load = ClassPathServiceLoader.load(CommandExecutorLoader.class);
        this.commandExecutorLoaders = Lists.newArrayList(load)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();
    }

    @Override
    public void onRefreshComplete(CoreTest coreTest) throws Throwable {
        Container container = coreTest.getContainer();
        if (container != null) {
            Object testInstance = coreTest.getTestInstance();
            if (testInstance instanceof CommandExecutorSetter<? extends AggregateCommandExecutor> setter
                    && !commandExecutorLoaders.isEmpty()) {
                ExecutorOptions executorOptions = drain(container);
                if (executorOptions != null) {
                    setter.setCommandExecutor(loadCommandExecutor(executorOptions));
                }
            }
        }
    }

    /**
     * load generic E command executor if non error throwing
     *
     * @param executorOptions the executorOptions
     * @param <E>             {@link AggregateCommandExecutor} sub-type
     * @return E or null
     */
    <E extends AggregateCommandExecutor> E loadCommandExecutor(ExecutorOptions executorOptions) {
        DBType dbType = executorOptions.getDbType();
        try {
            return (E) commandExecutorLoaders.stream()
                    .filter(Objects::nonNull)
                    .filter(loader -> loader.match(dbType))
                    .map(loader -> loader.load(executorOptions))
                    .findFirst()
                    .orElse(null);
        } catch (Throwable ex) {
            log.error("Failed to load database type [{}] to command executor", dbType, ex);
        }
        return null;
    }

    /**
     * pickup {@link Container} and return {@link ExecutorOptions}
     *
     * @param container container
     * @return ExecutorOptions
     */
    private ExecutorOptions drain(Container container) {
        ContainerType containerType = container.getContainerType();
        ContainerExecutorOptionsTranslator translator = translatorMap.get(containerType);
        if (translator == null) {
            return null;
        }
        DBType dbType = translator.withDBType(container);
        ExecutorKey executorKey = translator.withExecutorKey(container);
        OperatorKey operatorKey = translator.withOperatorKey(container);
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(dbType, executorKey, operatorKey);
        String address = translator.withAddress(container);
        executorOptions.setAddress(address);
        String database = translator.withDatabase(container);
        String username = translator.withUsername(container);
        String password = translator.withPassword(container);
        executorOptions.setDatabase(database);
        executorOptions.setUsername(username);
        executorOptions.setPassword(password);
        boolean isDefault = translator.withDefault();
        executorOptions.setSystemDefault(isDefault);
        return executorOptions;
    }
}