package cc.allio.uno.data.orm.sql.ddl.elasticsearch;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLDropTableOperator;

/**
 * 删除索引{@link DeleteIndexRequest}
 *
 * @author jiangwei
 * @date 2023/5/29 18:43
 * @since 1.1.4
 */
public class ElasticSearchDropIndexOperator implements SQLDropTableOperator {

    private DeleteIndexRequest deleteIndexRequest;
    private DeleteIndexRequest.Builder builder;
    private static final String ERROR_MSG = "elasticsearch drop operator not support that operator";

    public ElasticSearchDropIndexOperator() {
        builder = new DeleteIndexRequest.Builder();
    }

    @Override
    public String getSQL() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLDropTableOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        deleteIndexRequest = null;
        builder = new DeleteIndexRequest.Builder();
    }

    @Override
    public SQLDropTableOperator from(Table table) {
        builder.index(table.getName().format());
        return self();
    }

    @Override
    public SQLDropTableOperator ifExist(Boolean ifExist) {
        throw new SQLException(ERROR_MSG);
    }

    /**
     * 获取删除index请求
     *
     * @return DeleteIndexRequest
     */
    public DeleteIndexRequest getDeleteIndexRequest() {
        if (deleteIndexRequest == null) {
            deleteIndexRequest = builder.build();
        }
        return deleteIndexRequest;
    }
}
