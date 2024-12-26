package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;

/**
 * database relevant operator
 *
 * @author j.x
 * @since 1.1.7
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
