package cc.allio.uno.data.orm.dsl.elasticsearch.dml;

import cc.allio.uno.auto.service.AutoService;
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
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * 基于{@link SearchRequest}的查询，SQL数据为elasticsearch dsl
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(QueryOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsQueryOperator extends EsWhereOperatorImpl<EsQueryOperator> implements QueryOperator<EsQueryOperator> {

    private final DBType dbType;
    private SearchRequest searchRequest;
    private SearchRequest.Builder searchBuilder;
    private Table table;
    private List<String> columns = Lists.newArrayList();

    private static final String ERROR_MSG = "elasticsearch query operator not support that operator";

    public EsQueryOperator() {
        super();
        this.dbType = DBType.ELASTICSEARCH;
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
        return StringPool.EMPTY;
    }

    @Override
    public EsQueryOperator parse(String dsl) {
        // nothing to do
        return self();
    }

    @Override
    public EsQueryOperator customize(UnaryOperator<EsQueryOperator> operatorFunc) {
        return operatorFunc.apply(new EsQueryOperator());
    }

    @Override
    public void reset() {
        super.reset();
        searchRequest = null;
        searchBuilder = new SearchRequest.Builder();
        columns = Lists.newArrayList();
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing to do
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return List.of();
    }

    @Override
    public EsQueryOperator from(Table table) {
        this.table = table;
        searchBuilder = searchBuilder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public EsQueryOperator select(DSLName dslName) {
        return this.selects(Collections.singleton(dslName));
    }

    @Override
    public EsQueryOperator select(DSLName dslName, String alias) {
        return this.selects(Collections.singleton(dslName));
    }

    @Override
    public EsQueryOperator selects(Collection<DSLName> dslNames) {
        for (DSLName dslName : dslNames) {
            String name = dslName.format();
            FieldAndFormat ff = FieldAndFormat.of(f -> f.field(name));
            searchBuilder = searchBuilder.fields(ff);
            this.columns.add(name);
        }
        return self();
    }

    @Override
    public List<String> obtainSelectColumns() {
        return columns;
    }

    @Override
    public EsQueryOperator distinct() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsQueryOperator distinctOn(DSLName dslName, String alias) {
        Aggregation aggregation = Aggregation.of(a -> a.cardinality(v -> v.field(dslName.format())));
        searchBuilder.aggregations(alias, aggregation);
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsQueryOperator aggregate(Func syntax, DSLName dslName, String alias, Distinct distinct) {
        Aggregation aggregation = Aggregation.of(a -> {
            switch (syntax) {
                case COUNT_FUNCTION:
                    return a.valueCount(v -> v.field(dslName.format()));
                case MIN_FUNCTION:
                    return a.min(m -> m.field(dslName.format()));
                case AVG_FUNCTION:
                    return a.avg(m -> m.field(dslName.format()));
                case SUM_FUNCTION:
                    return a.sum(s -> s.field(dslName.format()));
                case MAX_FUNCTION:
                    return a.max(m -> m.field(dslName.format()));
                default:
                    return null;
            }
        });
        searchBuilder.aggregations(alias, aggregation);
        if (distinct != null) {
            Aggregation d = Aggregation.of(a -> a.cardinality(v -> v.field(dslName.format())));
            searchBuilder.aggregations(alias + "d", d);
        }
        return self();
    }

    @Override
    public EsQueryOperator from(QueryOperator<?> fromTable, String alias) {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsQueryOperator join(Table left, JoinType joinType, Table right, BinaryCondition condition) {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsQueryOperator orderBy(DSLName dslName, OrderCondition orderCondition) {
        SortOrder sortOrder;
        switch (orderCondition) {
            case ASC:
                sortOrder = SortOrder.Asc;
                break;
            case DESC:
            default:
                sortOrder = SortOrder.Desc;
        }
        SortOptions sortOptions = SortOptions.of(sp -> sp.field(fs -> fs.field(dslName.format()).order(sortOrder)));
        searchBuilder.sort(Collections.singletonList(sortOptions));
        return self();
    }

    @Override
    public EsQueryOperator limit(Long limit, Long offset) {
        searchBuilder.from(Math.toIntExact(limit));
        searchBuilder.size(Math.toIntExact(offset));
        return self();
    }

    @Override
    public EsQueryOperator groupByOnes(Collection<DSLName> fieldNames) {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsQueryOperator tree(QueryOperator<?> query) {
        return self();
    }

    @Override
    public EsQueryOperator tree(QueryOperator<?> baseQuery, QueryOperator<?> subQuery) {
        // nothing to do
        return self();
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
        if (CollectionUtils.isEmpty(mustQuery)
                && CollectionUtils.isEmpty(mustNotQuery)
                && CollectionUtils.isEmpty(shouldQuery)) {
            matchAll();
        }
    }
}
