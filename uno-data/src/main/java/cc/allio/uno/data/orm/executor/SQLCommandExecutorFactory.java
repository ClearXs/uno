package cc.allio.uno.data.orm.executor;

import cc.allio.uno.core.env.Envs;
import cc.allio.uno.data.orm.executor.elasticsearch.EsSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.influxdb.InfluxDBSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.mangodb.MangoDBSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.mybatis.MybatisSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.neo4j.Neo4jSQLCommandExecutor;
import cc.allio.uno.data.orm.executor.redis.RedisSQLCommandExecutor;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * SQL Executor simple Factory
 *
 * @author jiangwei
 * @date 2023/4/16 23:38
 * @since 1.1.4
 */
public class SQLCommandExecutorFactory {

    private static final Map<SQLCommandExecutor.ExecutorKey, SQLCommandExecutor> CACHES = Maps.newHashMap();

    private SQLCommandExecutorFactory() {
    }

    public static <T extends SQLCommandExecutor> T create(SQLCommandExecutor.ExecutorKey executorKey, Object... values) {
        if (SQLCommandExecutor.MYBATIS_SQL_COMMAND_EXECUTOR_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new MybatisSQLCommandExecutor(values));
        } else if (SQLCommandExecutor.ELASTICSEARCH_SQL_COMMAND_EXECUTOR_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new EsSQLCommandExecutor(values));
        } else if (SQLCommandExecutor.INFLUXDB_SQL_COMMAND_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new InfluxDBSQLCommandExecutor());
        } else if (SQLCommandExecutor.MONGODB_SQL_COMMAND_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new MangoDBSQLCommandExecutor());
        } else if (SQLCommandExecutor.NEO4J_SQL_COMMAND_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new Neo4jSQLCommandExecutor());
        } else if (SQLCommandExecutor.REDIS_SQL_COMMAND_KEY == executorKey) {
            return (T) CACHES.computeIfAbsent(executorKey, k -> new RedisSQLCommandExecutor());
        }
        // 默认为mybatis
        Envs.setProperty(SQLCommandExecutor.SQL_EXECUTOR_TYPE_KEY, SQLCommandExecutor.MYBATIS_SQL_COMMAND_EXECUTOR_KEY.getKey());
        return (T) new MybatisSQLCommandExecutor();
    }

    /**
     * 获取{@link SQLCommandExecutor}实例，默认为{@link MybatisSQLCommandExecutor}
     *
     * @return SQLExecutor
     */
    public static <T extends SQLCommandExecutor> T getSQLExecutor() {
        return getSQLExecutor(SQLCommandExecutor.getSystemExecutorKey());
    }

    /**
     * 获取{@link SQLCommandExecutor}实例，默认为{@link MybatisSQLCommandExecutor}
     *
     * @param executorKey 判断使用何种执行器key
     * @return SQLExecutor
     */
    public static <T extends SQLCommandExecutor> T getSQLExecutor(SQLCommandExecutor.ExecutorKey executorKey) {
        return (T) CACHES.get(executorKey);
    }
}
