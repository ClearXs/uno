package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link DeleteOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorDeleteOperator implements DeleteOperator<MetaAcceptorDeleteOperator>, WrapperOperator {

    private DeleteOperator<?> actual;
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
    public MetaAcceptorDeleteOperator from(Table table) {
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
    public MetaAcceptorDeleteOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator customize(UnaryOperator operatorFunc) {
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
    public MetaAcceptorDeleteOperator gt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator gte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator lt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator lte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator eq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.eq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator neq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.neq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator notNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.notNull(dslName);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator isNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.isNull(dslName);
        return self();
    }

    @Override
    public <V> MetaAcceptorDeleteOperator in(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.in(dslName, values);
        return self();
    }

    @Override
    public <V> MetaAcceptorDeleteOperator notIn(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.notIn(dslName, values);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator between(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.between(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator notBetween(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.notBetween(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator $like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator $like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator $notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator $notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator or() {
        actual.or();
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator and() {
        actual.and();
        return self();
    }

    @Override
    public MetaAcceptorDeleteOperator nor() {
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
