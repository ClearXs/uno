package cc.allio.uno.data.orm.sql.ddl.elasticsearch;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLExistTableOperator;

import java.util.List;

/**
 * 判断索引是否存在{@link co.elastic.clients.elasticsearch.indices.ExistsRequest}
 *
 * @author jiangwei
 * @date 2023/5/29 18:59
 * @since 1.1.4
 */
public class ElasticSearchExistIndexOperator implements SQLExistTableOperator {

    private SearchRequest searchRequest;
    SearchRequest.Builder builder;
    private static final String ERROR_MSG = "elasticsearch drop operator not support that operator";

    public ElasticSearchExistIndexOperator() {
        builder = new SearchRequest.Builder();
    }

    @Override
    public String getSQL() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLExistTableOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        searchRequest = null;
        builder = new SearchRequest.Builder();
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
    public SQLExistTableOperator from(Table table) {
        builder.index(table.getName().format());
        return self();
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
