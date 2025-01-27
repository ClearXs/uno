package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.core.util.map.OptionalMap;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.handler.ExecutorResultHandlerSet;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;

import java.util.List;

/**
 * 执行器{@link cc.allio.uno.data.orm.executor.CommandExecutor}相关参数
 *
 * @author j.x
 * @since 1.1.7
 */
public interface ExecutorOptions extends ExecutorResultHandlerSet, MetaAcceptorSet, OptionalMap<String> {

    String KEY_MARK = "key";
    String DB_TYPE_MARK = "dbType";
    String EXECUTOR_KEY_MARK = "executor";
    String OPERATOR_KEY_MARK = "operator";
    String USERNAME_MARK = "username";
    String PASSWORD_MARK = "password";
    String DATABASE_MARK = "database";
    String ADDRESS_MARK = "address";
    String SYSTEM_DEFAULT_MARK = "systemDefault";

    /**
     * 唯一标识
     *
     * @return key
     */
    default String getKey() {
        return getForce(KEY_MARK, String.class);
    }

    /**
     * set key
     *
     * @param key key
     */
    default void setKey(String key) {
        put(KEY_MARK, key);
    }

    /**
     * 获取db type
     */
    default DBType getDbType() {
        return getForce(DB_TYPE_MARK, DBType.class);
    }

    /**
     * 获取{@link ExecutorKey}
     */
    default ExecutorKey getExecutorKey() {
        return getForce(EXECUTOR_KEY_MARK, ExecutorKey.class);
    }

    /**
     * 获取{@link OperatorKey}
     */
    default OperatorKey getOperatorKey() {
        return getForce(OPERATOR_KEY_MARK, OperatorKey.class);
    }

    /**
     * 获取 data username
     */
    default String getUsername() {
        return getForce(USERNAME_MARK, String.class);
    }

    /**
     * set data username
     *
     * @param username username
     */
    default void setUsername(String username) {
        put(USERNAME_MARK, username);
    }

    /**
     * 获取 data address
     */
    default String getAddress() {
        return getForce(ADDRESS_MARK, String.class);
    }

    /**
     * set data address
     *
     * @param address address
     */
    default void setAddress(String address) {
        put(ADDRESS_MARK, address);
    }

    /**
     * 获取 data password
     */
    default String getPassword() {
        return getForce(PASSWORD_MARK, String.class);
    }

    /**
     * set data password
     *
     * @param password password
     */
    default void setPassword(String password) {
        put(PASSWORD_MARK, password);
    }

    /**
     * 获取 db database
     */
    default String getDatabase() {
        return getForce(DATABASE_MARK, String.class);
    }

    /**
     * set data database
     *
     * @param database database
     */
    default void setDatabase(String database) {
        put(DATABASE_MARK, database);
    }

    /**
     * 是否默认
     */
    default boolean isSystemDefault() {
        return getForce(SYSTEM_DEFAULT_MARK, Boolean.class);
    }

    /**
     * set system default
     * <p>
     *     it will be change system {@link OperatorKey} and {@link ExecutorKey}
     * </p>
     */
    default void setSystemDefault(boolean systemDefault) {
        if (systemDefault) {
            OperatorKey operatorKey = getOperatorKey();
            if (operatorKey != null) {
                Envs.setProperty(OperatorKey.OPERATOR_METADATA_KEY, operatorKey.key());
            }

            ExecutorKey executorKey = getExecutorKey();
            if (executorKey != null) {
                Envs.setProperty(ExecutorKey.DSL_EXECUTOR_TYPE_KEY, executorKey.key());
            }
        }

        put(SYSTEM_DEFAULT_MARK, systemDefault);
    }

    /**
     * 添加拦截器
     *
     * @param interceptor interceptor
     */
    void addInterceptor(Interceptor interceptor);

    /**
     * 添加拦截器
     *
     * @param interceptors interceptors
     */
    void addInterceptors(List<Interceptor> interceptors);

    /**
     * 获取{@link Interceptor} list
     */
    List<Interceptor> getInterceptors();

    /**
     * get {@link SPIInnerCommandScanner} instance
     *
     * @return {@link SPIInnerCommandScanner} instance
     */
    SPIInnerCommandScanner getScanner();
}
