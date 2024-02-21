package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;

/**
 * database relevant operator
 *
 * @author jiangwei
 * @date 2024/2/15 11:43
 * @since 1.1.6
 */
public interface DataBaseOperator<T extends DataBaseOperator<T>> extends Self<T> {

    /**
     * @see #database(DSLName)
     */
    default T database(String database) {
        return database(DSLName.of(database));
    }

    /**
     * @see #database(Database)
     */
    default T database(DSLName database) {
        return database(Database.of(database));
    }

    /**
     * set database
     *
     * @param database database
     * @return Self
     */
    T database(Database database);
}
