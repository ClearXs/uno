package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * DSL修改表以及相关字段操作
 *
 * @author j.x
 * @date 2023/6/8 19:21
 * @see OperatorGroup
 * @since 1.1.4
 */
public interface AlterTableOperator<T extends AlterTableOperator<T>> extends Operator<T>, Self<T>, TableOperator<T> {

    /**
     * @see #alertColumns(Collection)
     */
    default T alertColumn(ColumnDef columnDef) {
        return alertColumns(columnDef);
    }

    /**
     * @see #alertColumns(Collection)
     */
    default T alertColumns(ColumnDef... columnDefs) {
        return alertColumns(Arrays.stream(columnDefs).toList());
    }

    /**
     * 修改列
     *
     * @param columnDefs columnDefs
     * @return self
     */
    T alertColumns(Collection<ColumnDef> columnDefs);

    /**
     * @see #addColumns(Collection)
     */
    default T addColumn(ColumnDef columnDef) {
        return addColumns(columnDef);
    }

    /**
     * @see #addColumns(Collection)
     */
    default T addColumns(ColumnDef... columnDefs) {
        return addColumns(Arrays.stream(columnDefs).toList());
    }

    /**
     * 添加column
     *
     * @param columnDefs columnDefs
     * @return self
     */
    T addColumns(Collection<ColumnDef> columnDefs);

    /**
     * @see #deleteColumn(DSLName)
     */
    default T deleteColumn(String column) {
        return deleteColumn(DSLName.of(column));
    }

    /**
     * @see #deleteColumns(Collection)
     */
    default T deleteColumn(DSLName column) {
        return deleteColumns(column);
    }

    /**
     * @see #deleteColumns(Collection)
     */
    default T deleteColumns(DSLName... columns) {
        return deleteColumns(Arrays.stream(columns).toList());
    }

    /**
     * 删除列
     *
     * @param columns columns
     * @return self
     */
    T deleteColumns(Collection<DSLName> columns);

    /**
     * @see #rename(Table)
     */
    default T rename(String table) {
        return rename(Table.of(table));
    }

    /**
     * @see #rename(Table)
     */
    default T rename(DSLName table) {
        return rename(Table.of(table));
    }

    /**
     * @see #rename(Table)
     */
    default <R> T rename(Class<R> pojoClass) {
        return rename(PojoWrapper.findTable(pojoClass));
    }

    /**
     * @see #rename(Table)
     */
    default T rename(UnaryOperator<Table> func) {
        return rename(func.apply(new Table()));
    }

    /**
     * 重命名表名
     *
     * @param to to
     * @return self
     */
    T rename(Table to);
}
