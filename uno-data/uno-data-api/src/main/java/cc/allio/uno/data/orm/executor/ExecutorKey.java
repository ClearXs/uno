package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ExecutorKey
 *
 * @author jiangwei
 * @date 2024/1/3 22:59
 * @since 1.1.6
 */
public interface ExecutorKey extends Key {

    String DSL_EXECUTOR_TYPE_KEY = "allio.uno.data.orm.executor.key";

    ExecutorKey DB = returnKey("db");
    ExecutorKey ELASTICSEARCH = returnKey(OperatorKey.ELASTICSEARCH);
    ExecutorKey INFLUXDB = returnKey(OperatorKey.INFLUXDB);
    ExecutorKey MONGODB = returnKey(OperatorKey.MONGODB);
    ExecutorKey NEO4j = returnKey(OperatorKey.NEO4j);
    ExecutorKey REDIS = returnKey(OperatorKey.REDIS);

    @Override
    default String getProperties() {
        return DSL_EXECUTOR_TYPE_KEY;
    }

    static ExecutorKey returnKey(String key) {
        return new DefaultExecutorKey(key);
    }

    static ExecutorKey returnKey(OperatorKey key) {
        return new DefaultExecutorKey(key);
    }

    /**
     * 获取系统默认executor key
     *
     * @return ExecutorKey
     */
    static ExecutorKey getSystemExecutorKey() {
        String executorKey = Envs.getProperty(DSL_EXECUTOR_TYPE_KEY);
        return ExecutorKey.returnKey(executorKey);
    }

    @Data
    @EqualsAndHashCode(of = "key", callSuper = false)
    class DefaultExecutorKey implements ExecutorKey {
        private final String key;

        public DefaultExecutorKey(String key) {
            this.key = key;
        }

        public DefaultExecutorKey(OperatorKey operatorKey) {
            this.key = operatorKey.key();
        }

        @Override
        public String key() {
            return key;
        }
    }
}
