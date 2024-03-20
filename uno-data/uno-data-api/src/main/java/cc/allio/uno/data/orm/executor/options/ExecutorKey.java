package cc.allio.uno.data.orm.executor.options;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ExecutorKey
 *
 * @author j.x
 * @date 2024/1/3 22:59
 * @since 1.1.7
 */
public interface ExecutorKey extends Key {

    String DSL_EXECUTOR_TYPE_KEY = "allio.uno.data.orm.executor.key";

    String DB_LITERAL = "db";
    ExecutorKey DB = returnKey(DB_LITERAL);

    String ELASTICSEARCH_LITERAL = OperatorKey.ELASTICSEARCH_LITERAL;
    ExecutorKey ELASTICSEARCH = returnKey(ELASTICSEARCH_LITERAL);

    String INFLUXDB_LITERAL = OperatorKey.INFLUXDB_LITERAL;
    ExecutorKey INFLUXDB = returnKey(INFLUXDB_LITERAL);

    String MONGODB_LITERAL = OperatorKey.MONGODB_LITERAL;
    ExecutorKey MONGODB = returnKey(MONGODB_LITERAL);

    String NEO4J_LITERAL = OperatorKey.NEO4j_LITERAL;
    ExecutorKey NEO4j = returnKey(NEO4J_LITERAL);

    String REDIS_LITERAL = OperatorKey.REDIS_LITERAL;
    ExecutorKey REDIS = returnKey(REDIS_LITERAL);

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
