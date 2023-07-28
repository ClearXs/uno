package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLWhereOperator;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.sql.Operator;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * es通用条件查询
 *
 * @author jiangwei
 * @date 2023/5/29 13:22
 * @since 1.1.4
 */
public abstract class ElasticSearchGenericWhereOperator<T extends Operator<T>> implements SQLWhereOperator<T> {

    protected BoolQuery.Builder boolBuilder;

    protected final List<Query> mustQuery;
    protected final List<Query> shouldQuery;
    protected final List<Query> mustNotQuery;

    private static final String MUST_CONDITION = "must";
    private static final String SHOULD_CONDITION = "should";
    private static final String MUST_NOT_CONDITION = "must_not";
    private static final AtomicReference<String> LOGIC_PREDICATE = new AtomicReference<>(MUST_CONDITION);

    protected ElasticSearchGenericWhereOperator() {
        boolBuilder = new BoolQuery.Builder();
        this.mustQuery = Lists.newArrayList();
        this.shouldQuery = Lists.newArrayList();
        this.mustNotQuery = Lists.newArrayList();
    }

    @Override
    public T gt(SQLName sqlName, Object value) {
        return logicQuery(RangeQuery.of(rb -> rb.field(sqlName.format()).gt(JsonData.of(value)))._toQuery());
    }

    @Override
    public T gte(SQLName sqlName, Object value) {
        return logicQuery(RangeQuery.of(rb -> rb.field(sqlName.format()).gte(JsonData.of(value)))._toQuery());
    }

    @Override
    public T lt(SQLName sqlName, Object value) {
        return logicQuery(RangeQuery.of(rb -> rb.field(sqlName.format()).lt(JsonData.of(value)))._toQuery());
    }

    @Override
    public T lte(SQLName sqlName, Object value) {
        return logicQuery(RangeQuery.of(rb -> rb.field(sqlName.format()).lte(JsonData.of(value)))._toQuery());
    }

    @Override
    public T eq(SQLName sqlName, Object value) {
        FieldValue fieldValue = esValue(value);
        return logicQuery(MatchQuery.of(tq -> tq.field(sqlName.format()).query(fieldValue))._toQuery());
    }

    @Override
    public T notNull(SQLName sqlName) {
        return logicQuery(ExistsQuery.of(eq -> eq.field(sqlName.format()))._toQuery());
    }

    @Override
    public T isNull(SQLName sqlName) {
        Query query = ExistsQuery.of(eq -> eq.field(sqlName.format()))._toQuery();
        mustNotQuery.add(query);
        return self();
    }

    @Override
    public T in(SQLName sqlName, Object... values) {
        List<FieldValue> fieldValues = Arrays.stream(values).map(this::esValue).collect(Collectors.toList());
        return logicQuery(TermsQuery.of(tq -> tq.field(sqlName.format()).terms(TermsQueryField.of(tqf -> tqf.value(fieldValues))))._toQuery());
    }

    @Override
    public T between(SQLName sqlName, Object withValue, Object endValue) {
        return logicQuery(RangeQuery.of(rb -> rb.field(sqlName.format()).gte(JsonData.of(withValue)).lte(JsonData.of(endValue)))._toQuery());
    }

    @Override
    public T notBetween(SQLName sqlName, Object withValue, Object endValue) {
        mustNotQuery.add(RangeQuery.of(rb -> rb.field(sqlName.format()).gte(JsonData.of(withValue)).lte(JsonData.of(endValue)))._toQuery());
        return self();
    }

    @Override
    public T like(SQLName sqlName, Object value) {
        // match
        FieldValue fieldValue = esValue(value);
        return logicQuery(MatchQuery.of(mq -> mq.field(sqlName.format()).query(fieldValue))._toQuery());
    }

    @Override
    public T $like(SQLName sqlName, Object value) {
        // wildcard
        return logicQuery(WildcardQuery.of(wq -> wq.field(sqlName.format()).value(StringPool.STAR + value.toString()))._toQuery());
    }

    @Override
    public T like$(SQLName sqlName, Object value) {
        // wildcard
        return logicQuery(WildcardQuery.of(wq -> wq.field(sqlName.format()).value(value.toString() + StringPool.STAR))._toQuery());
    }

    @Override
    public T $like$(SQLName sqlName, Object value) {
        // wildcard
        return logicQuery(WildcardQuery.of(wq -> wq.field(sqlName.format()).value(StringPool.STAR + value.toString() + StringPool.STAR))._toQuery());
    }

    @Override
    public T or() {
        LOGIC_PREDICATE.set(SHOULD_CONDITION);
        return self();
    }

    @Override
    public T and() {
        LOGIC_PREDICATE.set(MUST_CONDITION);
        return self();
    }

    /**
     * and not <condition>
     *
     * @return ElasticsearchQueryOperator
     */
    public T andNot() {
        LOGIC_PREDICATE.set(MUST_NOT_CONDITION);
        return self();
    }

    /**
     * 查询所有
     *
     * @return T
     */
    public T matchAll() {
        Query query = MatchAllQuery.of(maq -> maq)._toQuery();
        return logicQuery(query);
    }

    /**
     * 返回esvalue数据
     *
     * @param value value
     * @return FieldValue
     */
    private FieldValue esValue(Object value) {
        FieldValue fieldValue;
        if (Types.isBoolean(value.getClass())) {
            fieldValue = FieldValue.of(Types.parseBoolean(value));
        } else if (Types.isShort(value.getClass()) || Types.isInteger(value.getClass()) || Types.isLong(value.getClass())) {
            fieldValue = FieldValue.of(Types.getLong(value));
        } else if (Types.isFloat(value.getClass()) || Types.isDouble(value.getClass())) {
            fieldValue = FieldValue.of(Types.getDouble(value));
        } else if (Types.isString(value.getClass())) {
            fieldValue = FieldValue.of(Types.getString(value));
        } else {
            fieldValue = FieldValue.NULL;
        }
        return fieldValue;
    }

    /**
     * 根据{@link #MUST_CONDITION}、{@link #SHOULD_CONDITION}、{@link #MUST_NOT_CONDITION}确定逻辑查询条件
     *
     * @return T
     */
    private T logicQuery(Query query) {
        if (LOGIC_PREDICATE.get().equals(MUST_CONDITION)) {
            mustQuery.add(query);
        } else if (LOGIC_PREDICATE.get().equals(SHOULD_CONDITION)) {
            shouldQuery.add(query);
        } else if (LOGIC_PREDICATE.get().equals(MUST_NOT_CONDITION)) {
            mustNotQuery.add(query);
        }
        return self();
    }

    public void reset() {
        boolBuilder = new BoolQuery.Builder();
        mustQuery.clear();
        shouldQuery.clear();
        mustNotQuery.clear();
    }

    /**
     * 根据当前条件构建{@link Query}条件
     *
     * @return Query for instance
     */
    protected Query buildQuery() {
        if (CollectionUtils.isNotEmpty(mustQuery)) {
            boolBuilder.must(mustQuery);
        }
        if (CollectionUtils.isNotEmpty(mustNotQuery)) {
            boolBuilder.mustNot(mustNotQuery);
        }
        if (CollectionUtils.isNotEmpty(shouldQuery)) {
            boolBuilder.should(shouldQuery);
        }
        return Query.of(q -> q.bool(boolBuilder.build()));
    }
}
