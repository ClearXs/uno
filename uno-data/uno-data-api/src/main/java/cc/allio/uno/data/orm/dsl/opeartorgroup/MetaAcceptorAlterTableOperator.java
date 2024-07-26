package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link AlterTableOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 16:30
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorAlterTableOperator implements AlterTableOperator<MetaAcceptorAlterTableOperator>, WrapperOperator {

    private AlterTableOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public MetaAcceptorAlterTableOperator alertColumns(Collection<ColumnDef> columnDefs) {
        metaAcceptorSet.adapt(columnDefs);
        actual.alertColumns(columnDefs);
        return self();
    }

    @Override
    public MetaAcceptorAlterTableOperator addColumns(Collection<ColumnDef> columnDefs) {
        metaAcceptorSet.adapt(columnDefs);
        actual.addColumns(columnDefs);
        return self();
    }

    @Override
    public MetaAcceptorAlterTableOperator deleteColumns(Collection<DSLName> columns) {
        metaAcceptorSet.adapt(columns);
        actual.deleteColumns(columns);
        return self();
    }

    @Override
    public MetaAcceptorAlterTableOperator rename(Table to) {
        metaAcceptorSet.adapt(to);
        actual.rename(to);
        return self();
    }

    @Override
    public MetaAcceptorAlterTableOperator from(Table table) {
        metaAcceptorSet.adapt(table);
        actual.from(table);
        return self();
    }

    @Override
    public Table getTable() {
        return actual.getTable();
    }

    @Override
    public String getDSL() {
        return actual.getDSL();
    }

    @Override
    public MetaAcceptorAlterTableOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorAlterTableOperator customize(UnaryOperator operatorFunc) {
        this.actual = actual.customize(operatorFunc);
        return self();
    }

    @Override
    public void reset() {
        actual.reset();
    }

    @Override
    public void setDBType(DBType dbType) {
        actual.setDBType(dbType);
    }

    @Override
    public DBType getDBType() {
        return actual.getDBType();
    }

    @Override
    public MetaAcceptorSet obtainMetaAcceptorSet() {
        return metaAcceptorSet;
    }

    @Override
    public <T extends Operator<T>> T getActual() {
        return (T) actual;
    }

    @Override
    public List<Operator<?>> getBeforeOperatorList() {
        return actual.getBeforeOperatorList();
    }

    @Override
    public List<Operator<?>> getCompensateOperatorList() {
        return actual.getCompensateOperatorList();
    }

    @Override
    public List<Operator<?>> getPostOperatorList() {
        return actual.getPostOperatorList();
    }
}
