package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link ShowColumnsOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 16:21
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorShowColumnsOperator implements ShowColumnsOperator<MetaAcceptorShowColumnsOperator>, WrapperOperator {

    private ShowColumnsOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public QueryOperator<?> toQueryOperator() {
        return actual.toQueryOperator();
    }

    @Override
    public MetaAcceptorShowColumnsOperator database(Database database) {
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
    public MetaAcceptorShowColumnsOperator from(Table table) {
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
    public MetaAcceptorShowColumnsOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorShowColumnsOperator customize(UnaryOperator operatorFunc) {
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
