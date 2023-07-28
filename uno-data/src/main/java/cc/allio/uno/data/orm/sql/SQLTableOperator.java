package cc.allio.uno.data.orm.sql;

/**
 * Table Operator (from xxx)
 *
 * @author jiangwei
 * @date 2023/4/16 18:07
 * @since 1.1.4
 */
public interface SQLTableOperator<T extends Operator<T>> extends Operator<T> {

    /**
     * FROM XX
     *
     * @param name table name
     * @return Operator
     */
    default T from(String name) {
        return from(Table.of(name));
    }

    /**
     * FROM XX
     *
     * @param name  table name
     * @param alias table alias
     * @return Operator
     */
    default T from(String name, String alias) {
        return from(Table.of(name, alias));
    }

    /**
     * FROM XX
     *
     * @param entityClass entityClass
     * @return Operator
     */
    default <P> T from(Class<P> entityClass) {
        return from(new PojoWrapper<P>(entityClass).getTable());
    }

    /**
     * FROM XX
     *
     * @param table table
     * @return Operator
     */
    T from(Table table);
}
