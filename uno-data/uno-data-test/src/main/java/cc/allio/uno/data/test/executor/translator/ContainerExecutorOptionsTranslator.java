package cc.allio.uno.data.test.executor.translator;

import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import cc.allio.uno.test.testcontainers.Container;
import org.testcontainers.containers.GenericContainer;

/**
 * translate {@link Container} to {@link ExecutorOptions}
 *
 * @author j.x
 * @date 2024/3/20 01:02
 * @since 1.1.7
 */
public interface ContainerExecutorOptionsTranslator {

    /**
     * translate to {@link DBType}
     *
     * @param testContainer the not null testContainer
     * @return the {@link DBType}
     */
    DBType withDBType(Container testContainer);

    /**
     * translate to {@link ExecutorKey}
     *
     * @param testContainer the not null testContainer
     * @return the {@link ExecutorKey}
     */
    ExecutorKey withExecutorKey(Container testContainer);

    /**
     * translate to {@link OperatorKey}
     *
     * @param testContainer the not null testContainer
     * @return the {@link OperatorKey}
     */
    OperatorKey withOperatorKey(Container testContainer);

    /**
     * translate to db address
     *
     * @param testContainer the not null testContainer
     * @return the db address
     */
    default String withAddress(Container testContainer) {
        GenericContainer<?> internal = testContainer.getInternal();
        return internal.getHost();
    }

    /**
     * translate to database
     *
     * @param testContainer the not null testContainer
     * @return the database
     */
    String withDatabase(Container testContainer);

    /**
     * translate to db username
     *
     * @param testContainer the not null testContainer
     * @return the db username
     */
    String withUsername(Container testContainer);

    /**
     * translate to db password
     *
     * @param testContainer the not null testContainer
     * @return the db password
     */
    String withPassword(Container testContainer);

    /**
     * return is default database, the default is false
     */
    default boolean withDefault() {
        return false;
    }
}
