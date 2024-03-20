package cc.allio.uno.data.orm.dsl.ddl;

import cc.allio.uno.core.api.Self;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.helper.PojoWrapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

/**
 * DSL修改表以及相关字段操作
 *
 * @author j.x
 * @date 2023/6/8 19:21
 * @since 1.1.4
 * @see OperatorGroup
 */
public interface AlterTableOperator extends Operator<AlterTableOperator>, Self<AlterTableOperator>, TableOperator<AlterTableOperator> {

    /**
     * @see #alertColumns(Collection)
     */
    default AlterTableOperator alertColumn(ColumnDef columnDef) {
        return alertColumns(columnDef);
    }

    /**
     * @see #alertColumns(Collection)
     */
    default AlterTableOperator alertColumns(ColumnDef... columnDefs) {
        return alertColumns(Arrays.stream(columnDefs).toList());
    }

    /**
     * 修改列
     *
     * @param columnDefs columnDefs
     * @return AlterTableOperator
     */
    AlterTableOperator alertColumns(Collection<ColumnDef> columnDefs);

    /**
     * @see #addColumns(Collection)
     */
    default AlterTableOperator addColumn(ColumnDef columnDef) {
        return addColumns(columnDef);
    }

    /**
     * @see #addColumns(Collection)
     */
    default AlterTableOperator addColumns(ColumnDef... columnDefs) {
        return addColumns(Arrays.stream(columnDefs).toList());
    }

    /**
     * 添加column
     *
     * @param columnDefs columnDefs
     * @return AlterTableOperator
     */
    AlterTableOperator addColumns(Collection<ColumnDef> columnDefs);

    /**
     * @see #deleteColumn(DSLName)
     */
    default AlterTableOperator deleteColumn(String column) {
        return deleteColumn(DSLName.of(column));
    }

    /**
     * @see #deleteColumns(Collection)
     */
    default AlterTableOperator deleteColumn(DSLName column) {
        return deleteColumns(column);
    }

    /**
     * @see #deleteColumns(Collection)
     */
    default AlterTableOperator deleteColumns(DSLName... columns) {
        return deleteColumns(Arrays.stream(columns).toList());
    }

    /**
     * 删除列
     *
     * @param columns columns
     * @return AlterTableOperator
     */
    AlterTableOperator deleteColumns(Collection<DSLName> columns);

    /**
     * @see #rename(Table)
     */
    default AlterTableOperator rename(String table) {
        return rename(Table.of(table));
    }

    /**
     * @see #rename(Table)
     */
    default AlterTableOperator rename(DSLName table) {
        return rename(Table.of(table));
    }

    /**
     * @see #rename(Table)
     */
    default <R> AlterTableOperator rename(Class<R> pojoClass) {
        return rename(PojoWrapper.findTable(pojoClass));
    }

    /**
     * @see #rename(Table)
     */
    default AlterTableOperator rename(UnaryOperator<Table> func) {
        return rename(func.apply(new Table()));
    }

    /**
     * 重命名表名
     *
     * @param to to
     * @return AlterTableOperator
     */
    AlterTableOperator rename(Table to);
}
