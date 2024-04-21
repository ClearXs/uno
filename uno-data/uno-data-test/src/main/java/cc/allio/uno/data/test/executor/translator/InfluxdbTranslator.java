package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.testcontainers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InfluxDBContainer;

import java.util.Map;

/**
 * influxdb describe executor options
 *
 * @author j.x
 * @date 2024/4/1 16:47
 * @see org.testcontainers.containers.InfluxDBContainer
 * @since 1.1.8
 */
public class InfluxdbTranslator implements ContainerExecutorOptionsTranslator {

    static final String DOCKER_INFLUXDB_INIT_USERNAME = "DOCKER_INFLUXDB_INIT_USERNAME";
    static final String DOCKER_INFLUXDB_INIT_PASSWORD = "DOCKER_INFLUXDB_INIT_PASSWORD";
    static final String DOCKER_INFLUXDB_INIT_BUCKET = "DOCKER_INFLUXDB_INIT_BUCKET";
    static final String DOCKER_INFLUXDB_INIT_ORG = "DOCKER_INFLUXDB_INIT_ORG";
    static final String DOCKER_INFLUXDB_INIT_ADMIN_TOKEN = "DOCKER_INFLUXDB_INIT_ADMIN_TOKEN";

    @Override
    public DBType withDBType(Container testContainer) {
        return DBType.INFLUXDB;
    }

    @Override
    public ExecutorKey withExecutorKey(Container testContainer) {
        return ExecutorKey.INFLUXDB;
    }

    @Override
    public OperatorKey withOperatorKey(Container testContainer) {
        return OperatorKey.INFLUXDB;
    }

    @Override
    public String withAddress(Container testContainer) {
        GenericContainer<?> internal = testContainer.getInternal();
        return StringPool.HTTP + internal.getHost() + ":" + internal.getMappedPort(InfluxDBContainer.INFLUXDB_PORT);
    }

    @Override
    public String withDatabase(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_BUCKET);
    }

    @Override
    public String withUsername(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_USERNAME);
    }

    @Override
    public String withPassword(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_PASSWORD);
    }

    @Override
    public Map<String, Object> withOthers(Container testContainer) {
        return Map.of("organization", testContainer.getEnv(DOCKER_INFLUXDB_INIT_ORG), "token", testContainer.getEnv(DOCKER_INFLUXDB_INIT_ADMIN_TOKEN));
    }
}
