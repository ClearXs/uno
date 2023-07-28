package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import cc.allio.uno.data.orm.dialect.func.Func;
import cc.allio.uno.data.orm.sql.*;
import cc.allio.uno.data.orm.sql.word.Distinct;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.sql.dml.local.OrderCondition;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 基于{@link SearchRequest}的查询，SQL数据为elasticsearch dsl
 *
 * @author jiangwei
 * @date 2023/5/28 15:34
 * @since 1.1.4
 */
public class ElasticSearchQueryOperator extends ElasticSearchGenericWhereOperator<SQLQueryOperator> implements SQLQueryOperator {

    private SearchRequest searchRequest;
    private SearchRequest.Builder searchBuilder;

    private static final String ERROR_MSG = "elasticsearch query operator not support that operator";

    public ElasticSearchQueryOperator() {
        super();
        searchBuilder = new SearchRequest.Builder();
        // 默认查询所有数据
        searchBuilder.size(Integer.MAX_VALUE);
    }

    @Override
    public String getSQL() {
        SearchRequest request = getSearchRequest();
        if (request != null && request.query() != null) {
            String dsl = JsonpUtils.toString(request.query());
            return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
        }
        return null;
    }

    @Override
    public SQLQueryOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        searchRequest = null;
        searchBuilder = new SearchRequest.Builder();
    }

    @Override
    public String getPrepareSQL() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLQueryOperator from(Table table) {
        searchBuilder = searchBuilder.index(table.getName().format());
        return self();
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName) {
        return this.selects(Collections.singleton(sqlName));
    }

    @Override
    public SQLQueryOperator select(SQLName sqlName, String alias) {
        return this.selects(Collections.singleton(sqlName));
    }

    @Override
    public SQLQueryOperator selects(Collection<SQLName> sqlNames) {
        for (SQLName sqlName : sqlNames) {
            FieldAndFormat ff = FieldAndFormat.of(f -> f.field(sqlName.format()));
            searchBuilder = searchBuilder.fields(ff);
        }
        return self();
    }

    @Override
    public SQLQueryOperator distinct() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLQueryOperator distinctOn(SQLName sqlName, String alias) {
        Aggregation aggregation = Aggregation.of(a -> a.cardinality(v -> v.field(sqlName.format())));
        searchBuilder.aggregations(alias, aggregation);
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLQueryOperator aggregate(Func syntax, SQLName sqlName, String alias, Distinct distinct) {
        Aggregation aggregation = Aggregation.of(a -> {
            switch (syntax) {
                case COUNT_FUNCTION:
                    return a.valueCount(v -> v.field(sqlName.format()));
                case MIN_FUNCTION:
                    return a.min(m -> m.field(sqlName.format()));
                case AVG_FUNCTION:
                    return a.avg(m -> m.field(sqlName.format()));
                case SUM_FUNCTION:
                    return a.sum(s -> s.field(sqlName.format()));
                case MAX_FUNCTION:
                    return a.max(m -> m.field(sqlName.format()));
                default:
                    return null;
            }
        });
        searchBuilder.aggregations(alias, aggregation);
        if (distinct != null) {
            Aggregation d = Aggregation.of(a -> a.cardinality(v -> v.field(sqlName.format())));
            searchBuilder.aggregations(alias + "d", d);
        }
        return self();
    }

    @Override
    public SQLQueryOperator from(SQLQueryOperator fromTable, String alias) {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLQueryOperator join(Table left, JoinType joinType, Table right, SQLBinaryCondition condition) {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLQueryOperator orderBy(SQLName sqlName, OrderCondition orderCondition) {
        SortOrder sortOrder;
        switch (orderCondition) {
            case ASC:
                sortOrder = SortOrder.Asc;
                break;
            case DESC:
            default:
                sortOrder = SortOrder.Desc;
        }
        SortOptions sortOptions = SortOptions.of(sp -> sp.field(fs -> fs.field(sqlName.format()).order(sortOrder)));
        searchBuilder.sort(Collections.singletonList(sortOptions));
        return self();
    }

    @Override
    public SQLQueryOperator limit(Long limit, Long offset) {
        searchBuilder.from(Math.toIntExact(limit));
        searchBuilder.size(Math.toIntExact(offset));
        return self();
    }

    @Override
    public SQLQueryOperator groupByOnes(Collection<SQLName> fieldNames) {
        throw new SQLException(ERROR_MSG);
    }

    /**
     * 获取es{@link SearchRequest}实例
     *
     * @return query instance
     */
    public synchronized SearchRequest getSearchRequest() {
        if (searchRequest == null) {
            addDefaultQuery();
            Query query = buildQuery();
            searchRequest = searchBuilder.query(query).build();
        }
        return searchRequest;
    }

    /**
     * 添加默认查询：
     * <ul>
     *     <li>{@link #matchAll()}</li>
     * </ul>
     */
    private void addDefaultQuery() {
        if (CollectionUtils.isEmpty(mustQuery) && CollectionUtils.isEmpty(mustNotQuery) && CollectionUtils.isEmpty(shouldQuery)) {
            matchAll();
        }
    }
}
