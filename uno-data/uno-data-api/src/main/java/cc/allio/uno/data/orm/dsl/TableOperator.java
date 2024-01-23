package cc.allio.uno.data.orm.dsl;

import java.util.function.UnaryOperator;

/**
 * Table Operator (from xxx)
 *
 * @author jiangwei
 * @date 2023/4/16 18:07
 * @since 1.1.4
 */
public interface TableOperator<T extends Self<T>> extends Self<T> {

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
     * @param name table name
     * @return Operator
     */
    default T from(DSLName name) {
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
        return from(PojoWrapper.findTable(entityClass));
    }

    /**
     * FROM XX
     *
     * @param func func
     * @return Operator
     */
    default T from(UnaryOperator<Table> func) {
        return from(func.apply(new Table()));
    }

    /**
     * FROM XX
     *
     * @param table table
     * @return Operator
     */
    T from(Table table);

    /**
     * 获取table对象
     *
     * @return Table
     */
    Table getTable();
}
