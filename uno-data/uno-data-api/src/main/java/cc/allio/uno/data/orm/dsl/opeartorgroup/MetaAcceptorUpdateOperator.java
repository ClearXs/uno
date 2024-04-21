package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link UpdateOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 16:08
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorUpdateOperator implements UpdateOperator<MetaAcceptorUpdateOperator>, WrapperOperator {

    private UpdateOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public MetaAcceptorUpdateOperator updates(Map<DSLName, Object> values) {
        actual.updates(values);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator strictFill(String f, Supplier<Object> v) {
        actual.strictFill(f, v);
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
    public MetaAcceptorUpdateOperator from(Table table) {
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
    public MetaAcceptorUpdateOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator customize(UnaryOperator operatorFunc) {
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
    public MetaAcceptorUpdateOperator gt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator gte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator lt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator lte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator eq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.eq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator neq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.neq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator notNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.notNull(dslName);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator isNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.isNull(dslName);
        return self();
    }

    @Override
    public <V> MetaAcceptorUpdateOperator in(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.in(dslName, values);
        return self();
    }

    @Override
    public <V> MetaAcceptorUpdateOperator notIn(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.notIn(dslName, values);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator between(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.between(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator notBetween(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.notBetween(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator $like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator $like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator $notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator $notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator or() {
        actual.or();
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator and() {
        actual.and();
        return self();
    }

    @Override
    public MetaAcceptorUpdateOperator nor() {
        actual.nor();
        return self();
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
