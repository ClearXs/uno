package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * wrapper for {@link QueryOperator} from {@link MetaAcceptorSet}
 *
 * @author j.x
 * @date 2024/4/21 15:54
 * @since 1.1.8
 */
@AllArgsConstructor
public class MetaAcceptorQueryOperator implements QueryOperator<MetaAcceptorQueryOperator>, WrapperOperator {

    private QueryOperator<?> actual;
    @NotNull
    private final MetaAcceptorSet metaAcceptorSet;

    @Override
    public MetaAcceptorQueryOperator select(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.select(dslName);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator select(DSLName dslName, String alias) {
        metaAcceptorSet.adapt(dslName);
        actual.select(dslName, alias);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator selects(Collection<DSLName> dslNames) {
        metaAcceptorSet.adapt(dslNames);
        actual.selects(dslNames);
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return actual.obtainSelectColumns();
    }

    @Override
    public MetaAcceptorQueryOperator distinct() {
        actual.distinct();
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator distinctOn(DSLName dslName, String alias) {
        metaAcceptorSet.adapt(dslName);
        actual.distinctOn(dslName, alias);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        metaAcceptorSet.adapt(dslName);
        actual.aggregate(syntax, dslName, alias, distinct);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator from(QueryOperator<?> fromTable, String alias) {
        actual.from(fromTable, alias);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        metaAcceptorSet.adapt(left);
        metaAcceptorSet.adapt(right);
        actual.join(left, joinType, right, condition);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator orderBy(DSLName dslName, OrderCondition orderCondition) {
        metaAcceptorSet.adapt(dslName);
        actual.orderBy(dslName, orderCondition);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator limit(Long limit, Long offset) {
        actual.limit(limit, offset);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        metaAcceptorSet.adapt(fieldNames);
        actual.groupByOnes(fieldNames);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        actual.tree(baseQuery, subQuery);
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
    public MetaAcceptorQueryOperator from(Table table) {
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
    public MetaAcceptorQueryOperator parse(String dsl) {
        this.actual = actual.parse(dsl);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator customize(UnaryOperator operatorFunc) {
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
    public MetaAcceptorQueryOperator gt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator gte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.gte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator lt(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lt(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator lte(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.lte(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator eq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.eq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator neq(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.neq(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator notNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.notNull(dslName);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator isNull(DSLName dslName) {
        metaAcceptorSet.adapt(dslName);
        actual.isNull(dslName);
        return self();
    }

    @Override
    public <V> MetaAcceptorQueryOperator in(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.in(dslName, values);
        return self();
    }

    @Override
    public <V> MetaAcceptorQueryOperator notIn(DSLName dslName, V... values) {
        metaAcceptorSet.adapt(dslName);
        actual.notIn(dslName, values);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator between(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.between(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator notBetween(DSLName dslName, Object withValue, Object endValue) {
        metaAcceptorSet.adapt(dslName);
        actual.notBetween(dslName, withValue, endValue);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator $like(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator $like$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$like$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator $notLike(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator $notLike$(DSLName dslName, Object value) {
        metaAcceptorSet.adapt(dslName);
        actual.$notLike$(dslName, value);
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator or() {
        actual.or();
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator and() {
        actual.and();
        return self();
    }

    @Override
    public MetaAcceptorQueryOperator nor() {
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
