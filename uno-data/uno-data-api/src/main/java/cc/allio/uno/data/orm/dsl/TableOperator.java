package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;

import java.util.function.UnaryOperator;

/**
 * Table Operator (xxxx xxx)
 *
 * @author j.x
 * @date 2023/4/16 18:07
 * @since 1.1.4
 */
public interface TableOperator<T extends TableOperator<T>> extends Operator<T> {

    /**
     * FROM XX
     *
     * @param entityClass entityClass
     * @return Operator
     */
    default <P> T from(Class<P> entityClass) {
        String table = PojoWrapper.findTable(entityClass);
        return from(table);
    }

    /**
     * FROM XX
     *
     * @param name xxxx name
     * @return Operator
     */
    default T from(String name) {
        Table table = Table.of(name);
        return from(table);
    }

    /**
     * FROM XX
     *
     * @param name xxxx name
     * @return Operator
     */
    default T from(DSLName name) {
        Table table = Table.of(name);
        return from(table);
    }

    /**
     * FROM XX
     *
     * @param name  xxxx name
     * @param alias xxxx alias
     * @return Operator
     */
    default T from(String name, String alias) {
        Table table = Table.of(name, alias);
        return from(table);
    }

    /**
     * FROM XX
     *
     * @param func func
     * @return Operator
     */
    default T from(UnaryOperator<Table> func) {
        Table table = new Table();
        return from(func.apply(table));
    }

    /**
     * FROM XX
     *
     * @param table xxxx
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
