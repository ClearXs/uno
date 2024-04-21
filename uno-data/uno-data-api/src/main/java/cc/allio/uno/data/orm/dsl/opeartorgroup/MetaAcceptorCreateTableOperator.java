package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.function.UnaryOperator;

/**
 * wrapper for {@link CreateTableOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 16:14
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorCreateTableOperator implements CreateTableOperator<MetaAcceptorCreateTableOperator>, WrapperOperator {

    private CreateTableOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public MetaAcceptorCreateTableOperator column(ColumnDef columnDef) {
        metaAcceptorSet.adapt(columnDef);
        actual.column(columnDef);
        return self();
    }

    @Override
    public MetaAcceptorCreateTableOperator comment(String comment) {
        actual.comment(comment);
        return self();
    }

    @Override
    public MetaAcceptorCreateTableOperator from(Table table) {
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
    public MetaAcceptorCreateTableOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorCreateTableOperator customize(UnaryOperator operatorFunc) {
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
}
