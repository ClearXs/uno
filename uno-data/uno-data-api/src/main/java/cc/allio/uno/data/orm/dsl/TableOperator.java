package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;

import java.util.function.UnaryOperator;

/**
 * Table Operator (xxxx xxx)
 *
 * @author jiangwei
 * @date 2023/4/16 18:07
 * @since 1.1.4
 */
public interface TableOperator<T extends Self<T>> extends Self<T> {

    /**
     * FROM XX
     *
     * @param name xxxx name
     * @return Operator
     */
    default T from(String name) {
        return from(Table.of(name));
    }

    /**
     * FROM XX
     *
     * @param name xxxx name
     * @return Operator
     */
    default T from(DSLName name) {
        return from(Table.of(name));
    }

    /**
     * FROM XX
     *
     * @param name  xxxx name
     * @param alias xxxx alias
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
