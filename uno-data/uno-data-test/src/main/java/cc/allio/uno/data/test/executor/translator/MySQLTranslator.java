package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.test.testcontainers.Container;

/**
 * mysql impl translator
 *
 * @author j.x
 * @date 2024/3/20 01:07
 * @since 1.1.7
 */
public class MySQLTranslator extends RDBTranslator {

    @Override
    public DBType withDBType(Container testContainer) {
        return DBType.MYSQL;
    }

    @Override
    public String withDatabase(Container testContainer) {
        return testContainer.getEnv("MYSQL_DATABASE");
    }

    @Override
    public String withUsername(Container testContainer) {
        return testContainer.getEnv("MYSQL_USER");
    }

    @Override
    public String withPassword(Container testContainer) {
        return testContainer.getEnv("MYSQL_PASSWORD");
    }
}
