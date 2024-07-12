package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.PrepareValue;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link ExistTableOperator}
 *
 * @author j.x
 * @date 2024/4/21 16:19
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorExistTableOperator implements ExistTableOperator<MetaAcceptorExistTableOperator>, WrapperOperator {

    private ExistTableOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public String getPrepareDSL() {
        return actual.getPrepareDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return actual.getPrepareValues();
    }

    @Override
    public MetaAcceptorExistTableOperator from(Table table) {
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
    public MetaAcceptorExistTableOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorExistTableOperator customize(UnaryOperator operatorFunc) {
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
