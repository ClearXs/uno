package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link ShowTablesOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorShowTablesOperator implements ShowTablesOperator<MetaAcceptorShowTablesOperator>, WrapperOperator {

    private ShowTablesOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public QueryOperator<?> toQueryOperator() {
        return actual.toQueryOperator();
    }

    @Override
    public MetaAcceptorShowTablesOperator schema(String schema) {
        actual.schema(schema);
        return self();
    }

    @Override
    public MetaAcceptorShowTablesOperator from(Table table) {
        metaAcceptorSet.adapt(table);
        actual.from(table);
        return self();
    }

    @Override
    public Table getTable() {
        return actual.getTable();
    }

    @Override
    public MetaAcceptorShowTablesOperator database(Database database) {
        metaAcceptorSet.adapt(database);
        actual.database(database);
        return self();
    }

    @Override
    public String getPrepareDSL() {
        return actual.getPrepareDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return actual.getPrepareValues();
    }

    @Override
    public String getDSL() {
        return actual.getDSL();
    }

    @Override
    public MetaAcceptorShowTablesOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorShowTablesOperator customize(UnaryOperator operatorFunc) {
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
