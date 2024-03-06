package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.data.orm.executor.options.ExecutorOptionsImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * {@link ExecutorOptionsImpl}构建
 *
 * @author jiangwei
 * @date 2024/1/25 17:52
 * @since 1.1.7
 */
public class ExecutorOptionsBuilder implements Self<ExecutorOptionsBuilder> {

    private String key;
    private DBType dbType;
    private ExecutorKey executorKey;
    private OperatorKey operatorKey;
    private boolean systemDefault = false;
    private String address;
    private String username;
    private String password;
    private String database;
    private final List<Interceptor> interceptors = Lists.newArrayList();
    private final Map<String, Object> properties = Maps.newHashMap();

    public static ExecutorOptionsBuilder create() {
        return create(DBType.H2, IdGenerator.defaultGenerator().getNextIdAsString());
    }

    public static ExecutorOptionsBuilder create(String key) {
        return create(DBType.H2, key);
    }

    public static ExecutorOptionsBuilder create(DBType dbType) {
        return create(dbType, null);
    }

    public static ExecutorOptionsBuilder create(@NotNull DBType dbType, String key) {
        ExecutorOptionsBuilder builder = new ExecutorOptionsBuilder();
        if (StringUtils.isBlank(key)) {
            key = IdGenerator.defaultGenerator().getNextIdAsString();
        }
        builder.key = key;
        builder.dbType = dbType;
        builder.setProperty(ExecutorOptionsProperty.DB_TYPE, dbType);
        return builder;
    }

    /**
     * setValue executor key
     *
     * @param executorKey executorKey
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder executorKey(@NotNull ExecutorKey executorKey) {
        this.executorKey = executorKey;
        setProperty(ExecutorOptionsProperty.EXECUTOR_KEY, executorKey);
        return self();
    }

    /**
     * setValue operator key
     *
     * @param operatorKey operatorKey
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder operatorKey(@NotNull OperatorKey operatorKey) {
        this.operatorKey = operatorKey;
        setProperty(ExecutorOptionsProperty.OPERATOR_KEY, operatorKey);
        return self();
    }

    /**
     * setValue address, like localhost:1000, if cluster address is localhost:1000,localhost:1001 etc...
     *
     * @param address address
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder address(String address) {
        this.address = address;
        setProperty(ExecutorOptionsProperty.ADDRESS, address);
        return self();
    }

    /**
     * setValue username
     *
     * @param username username
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder username(String username) {
        this.username = username;
        setProperty(ExecutorOptionsProperty.USERNAME, username);
        return self();
    }

    /**
     * setValue password
     *
     * @param password password
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder password(String password) {
        this.password = password;
        setProperty(ExecutorOptionsProperty.PASSWORD, password);
        return self();
    }

    /**
     * setValue database
     *
     * @param database database
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder database(String database) {
        this.database = database;
        setProperty(ExecutorOptionsProperty.DATABASE, database);
        return self();
    }

    /**
     * setValue system default
     *
     * @param systemDefault systemDefault
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder systemDefault(boolean systemDefault) {
        this.systemDefault = systemDefault;
        setProperty(ExecutorOptionsProperty.SYSTEM_DEFAULT, systemDefault);
        return self();
    }

    /**
     * setValue interceptor
     *
     * @param interceptor interceptor
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder interceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return self();
    }

    /**
     * setValue interceptors
     *
     * @param interceptors interceptors
     * @return ExecutorOptionsBuilder
     */
    public ExecutorOptionsBuilder interceptors(Collection<Interceptor> interceptors) {
        this.interceptors.addAll(interceptors);
        return self();
    }

    /**
     * setValue options property
     *
     * @param key   属性名称
     * @param value 属性值
     */
    public ExecutorOptionsBuilder setProperty(String key, Object value) {
        properties.put(key, value);
        return self();
    }

    /**
     * build requirement is {@link #dbType} {@link #executorKey} {@link #operatorKey} not null
     *
     * @return ExecutorOptions
     * @throws IllegalArgumentException if {@code dbType} {@code executorKey} {@code operatorKey} is null
     */
    public ExecutorOptions build() {
        if (dbType == null || executorKey == null || operatorKey == null) {
            throw new IllegalArgumentException("dbType or executorKey or operatorKey is null");
        }
        ExecutorOptions executorOptions = new ExecutorOptionsImpl(key, dbType, executorKey, operatorKey);
        executorOptions.setSystemDefault(systemDefault);
        executorOptions.setAddress(address);
        executorOptions.setUsername(username);
        executorOptions.setPassword(password);
        executorOptions.setDatabase(database);
        executorOptions.putAll(properties);
        executorOptions.addInterceptors(interceptors);
        return executorOptions;
    }


    public interface ExecutorOptionsProperty {
        String DB_TYPE = "dbType";
        String EXECUTOR_KEY = "executorKey";
        String OPERATOR_KEY = "operatorKey";
        String ADDRESS = "address";
        String USERNAME = "username";
        String PASSWORD = "password";
        String DATABASE = "database";
        String SYSTEM_DEFAULT = "systemDefault";
    }
}
