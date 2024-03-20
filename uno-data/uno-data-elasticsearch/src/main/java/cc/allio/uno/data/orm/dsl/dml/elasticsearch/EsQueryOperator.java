package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Func;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.dsl.word.Distinct;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 基于{@link SearchRequest}的查询，SQL数据为elasticsearch dsl
 *
 * @author j.x
 * @date 2023/5/28 15:34
 * @since 1.1.4
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsQueryOperator extends EsGenericWhereOperator<QueryOperator> implements QueryOperator {

    private DBType dbType;
    private SearchRequest searchRequest;
    private SearchRequest.Builder searchBuilder;
    private Table table;

    private static final String ERROR_MSG = "elasticsearch query operator not support that operator";

    public EsQueryOperator() {
        super();
        this.dbType = DBType.ELASTIC_SEARCH;
        this.searchBuilder = new SearchRequest.Builder();
        // 默认查询所有数据
        this.searchBuilder.size(Integer.MAX_VALUE);
    }

    @Override
    public String getDSL() {
        SearchRequest request = getSearchRequest();
        if (request != null && request.query() != null) {
            String dsl = JsonpUtils.toString(request.query());
            return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
        }
        return null;
    }

    @Override
    public QueryOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        searchRequest = null;
        searchBuilder = new SearchRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        throw Exceptions.unOperate("setDBType");
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public QueryOperator from(Table table) {
        this.table = table;
        searchBuilder = searchBuilder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public QueryOperator select(DSLName sqlName) {
        return this.selects(Collections.singleton(sqlName));
    }

    @Override
    public QueryOperator select(DSLName sqlName, String alias) {
        return this.selects(Collections.singleton(sqlName));
    }

    @Override
    public QueryOperator selects(Collection<DSLName> sqlNames) {
        for (DSLName sqlName : sqlNames) {
            FieldAndFormat ff = FieldAndFormat.of(f -> f.field(sqlName.format()));
            searchBuilder = searchBuilder.fields(ff);
        }
        return self();
    }

    @Override
    public QueryOperator distinct() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public QueryOperator distinctOn(DSLName sqlName, String alias) {
        Aggregation aggregation = Aggregation.of(a -> a.cardinality(v -> v.field(sqlName.format())));
        searchBuilder.aggregations(alias, aggregation);
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public QueryOperator aggregate(Func syntax, DSLName sqlName, String alias, Distinct distinct) {
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
    public QueryOperator from(QueryOperator fromTable, String alias) {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public QueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public QueryOperator orderBy(DSLName sqlName, OrderCondition orderCondition) {
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
    public QueryOperator limit(Long limit, Long offset) {
        searchBuilder.from(Math.toIntExact(limit));
        searchBuilder.size(Math.toIntExact(offset));
        return self();
    }

    @Override
    public QueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        throw new DSLException(ERROR_MSG);
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

    @Override
    public QueryOperator neq(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public QueryOperator notIn(DSLName sqlName, Object... values) {
        return null;
    }

    @Override
    public QueryOperator notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public QueryOperator $notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public QueryOperator notLike$(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public QueryOperator $notLike$(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public QueryOperator nor() {
        return null;
    }
}
