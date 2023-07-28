package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.dml.SQLDeleteOperator;

import java.util.Collections;
import java.util.List;

/**
 * {@link DeleteByQueryRequest}实现删除数据
 *
 * @author jiangwei
 * @date 2023/5/29 13:49
 * @since 1.1.4
 */
public class ElasticSearchDeleteOperator extends ElasticSearchGenericWhereOperator<SQLDeleteOperator> implements SQLDeleteOperator {

    private DeleteByQueryRequest deleteRequest;
    private DeleteByQueryRequest.Builder deleteBuilder;
    private static final String ERROR_MSG = "elasticsearch delete operator not support that operator";

    public ElasticSearchDeleteOperator() {
        this.deleteBuilder = new DeleteByQueryRequest.Builder();
    }

    @Override
    public String getSQL() {
        DeleteByQueryRequest request = getDeleteRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public SQLDeleteOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.deleteRequest = null;
        this.deleteBuilder = new DeleteByQueryRequest.Builder();
    }

    @Override
    public String getPrepareSQL() {
        return null;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLDeleteOperator from(Table table) {
        deleteBuilder.index(Collections.singletonList(table.getName().format()));
        return self();
    }

    /**
     * 获取{@link DeleteByQueryRequest}实例
     *
     * @return DeleteByQueryRequest for instance
     */
    public DeleteByQueryRequest getDeleteRequest() {
        if (deleteRequest == null) {
            Query query = buildQuery();
            deleteRequest = deleteBuilder.query(query).build();
        }
        return deleteRequest;
    }
}
