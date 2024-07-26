package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.MetaAcceptorSet;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link DropTableOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 16:17
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorDropTableOperator implements DropTableOperator<MetaAcceptorDropTableOperator>, WrapperOperator {

    private DropTableOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public MetaAcceptorDropTableOperator ifExist(Boolean ifExist) {
        actual.ifExist(ifExist);
        return self();
    }

    @Override
    public MetaAcceptorDropTableOperator from(Table table) {
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
    public MetaAcceptorDropTableOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorDropTableOperator customize(UnaryOperator operatorFunc) {
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
