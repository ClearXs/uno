package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.test.testcontainers.Container;

/**
 * influxdb describe executor options
 *
 * @author j.x
 * @date 2024/4/1 16:47
 * @since 1.1.8
 */
public class InfluxdbTranslator implements ContainerExecutorOptionsTranslator {

    private static final String DOCKER_INFLUXDB_INIT_USERNAME = "DOCKER_INFLUXDB_INIT_USERNAME";
    private static final String DOCKER_INFLUXDB_INIT_PASSWORD = "DOCKER_INFLUXDB_INIT_PASSWORD";
    private static final String DOCKER_INFLUXDB_INIT_ORG = "DOCKER_INFLUXDB_INIT_ORG";

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
    public String withDatabase(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_ORG);
    }

    @Override
    public String withUsername(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_USERNAME);
    }

    @Override
    public String withPassword(Container testContainer) {
        return testContainer.getEnv(DOCKER_INFLUXDB_INIT_PASSWORD);
    }
}
