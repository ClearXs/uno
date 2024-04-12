package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.testcontainers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;

/**
 * mssql impl translator
 *
 * @author j.x
 * @date 2024/3/20 01:16
 * @since 1.1.7
 */
public class MSSQLTranslator extends RDBTranslator {

    @Override
    public String withAddress(Container testContainer) {
        String host = super.withAddress(testContainer);
        GenericContainer<?> internal = testContainer.getInternal();
        return host + StringPool.COLON + internal.getMappedPort(MSSQLServerContainer.MS_SQL_SERVER_PORT);
    }

    @Override
    public DBType withDBType(Container testContainer) {
        return DBType.SQLSERVER;
    }

    @Override
    public String withDatabase(Container testContainer) {
        return "sqlserver";
    }

    @Override
    public String withUsername(Container testContainer) {
        return "sa";
    }

    @Override
    public String withPassword(Container testContainer) {
        return testContainer.getEnv("SA_PASSWORD");
    }
}
