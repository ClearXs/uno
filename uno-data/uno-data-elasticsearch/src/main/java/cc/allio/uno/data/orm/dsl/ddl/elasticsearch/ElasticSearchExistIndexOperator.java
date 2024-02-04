package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;

import java.util.List;

/**
 * 判断索引是否存在{@link co.elastic.clients.elasticsearch.indices.ExistsRequest}
 *
 * @author jiangwei
 * @date 2023/5/29 18:59
 * @since 1.1.4
 */
@AutoService(ExistTableOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class ElasticSearchExistIndexOperator implements ExistTableOperator {
    SearchRequest.Builder builder;
    private SearchRequest searchRequest;
    private Table table;
    private static final String ERROR_MSG = "elasticsearch drop operator not support that operator";

    public ElasticSearchExistIndexOperator() {
        builder = new SearchRequest.Builder();
    }

    @Override
    public String getDSL() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public ExistTableOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        searchRequest = null;
        builder = new SearchRequest.Builder();
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
    public ExistTableOperator from(Table table) {
        this.table = table;
        builder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTables() {
        return table;
    }

    /**
     * 获取存在request
     *
     * @return ExistsRequest
     */
    public SearchRequest getSearchRequest() {
        if (searchRequest == null) {
            searchRequest = builder.build();
        }
        return searchRequest;
    }

}
