package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Key;
import cc.allio.uno.core.env.Envs;
import cc.allio.uno.core.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OperatorKey
 *
 * @author jiangwei
 * @date 2024/1/3 22:48
 * @since 1.1.6
 */
public interface OperatorKey extends Key {

    String OPERATOR_METADATA_KEY = "allio.uno.data.orm.dsl.operator.key";
    String SQL_LITERAL = "sql";
    OperatorKey SQL = returnKey(SQL_LITERAL);
    String ELASTICSEARCH_LITERAL = "elasticsearch";
    OperatorKey ELASTICSEARCH = returnKey(ELASTICSEARCH_LITERAL);
    String INFLUXDB_LITERAL = "influxdb";
    OperatorKey INFLUXDB = returnKey(INFLUXDB_LITERAL);
    String MONGODB_LITERAL = "mongodb";
    OperatorKey MONGODB = returnKey(MONGODB_LITERAL);
    String NEO4j_LITERAL = "neo4j";
    OperatorKey NEO4j = returnKey(NEO4j_LITERAL);
    String REDIS_LITERAL = "redis";
    OperatorKey REDIS = returnKey(REDIS_LITERAL);

    /**
     * 获取系统配置下的operator key
     *
     * @return operator key or default  DRUID_OPERATOR_KEY
     */
    static OperatorKey getSystemOperatorKey() {
        String operatorKey = Envs.getProperty(OPERATOR_METADATA_KEY);
        if (StringUtils.isBlank(operatorKey)) {
            return null;
        }
        return OperatorKey.returnKey(operatorKey);
    }

    @Override
    default String getProperties() {
        return OPERATOR_METADATA_KEY;
    }

    default boolean equalsTo(String key) {
        return this.key().equals(key);
    }

    static OperatorKey returnKey(String key) {
        return new DefaultOperatorKey(key);
    }

    @Data
    @EqualsAndHashCode(of = "key", callSuper = false)
    @AllArgsConstructor
    class DefaultOperatorKey implements OperatorKey {
        private final String key;

        @Override
        public String key() {
            return key;
        }
    }
}
