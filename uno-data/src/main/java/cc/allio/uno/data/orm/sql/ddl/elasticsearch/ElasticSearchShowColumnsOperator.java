package cc.allio.uno.data.orm.sql.ddl.elasticsearch;

import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.ddl.SQLShowColumnsOperator;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * es 查询所有映射.{@link co.elastic.clients.elasticsearch.indices.GetMappingRequest}
 *
 * @author jiangwei
 * @date 2023/6/11 20:07
 * @since 1.1.4
 */
public class ElasticSearchShowColumnsOperator implements SQLShowColumnsOperator {

    private GetMappingRequest.Builder builder;
    private GetMappingRequest getMappingRequest;

    public ElasticSearchShowColumnsOperator() {
        this.builder = new GetMappingRequest.Builder();
    }

    @Override
    public String getSQL() {
        throw new SQLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public SQLShowColumnsOperator parse(String sql) {
        throw new SQLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public void reset() {
        builder = new GetMappingRequest.Builder();
        getMappingRequest = null;
    }

    @Override
    public String getPrepareSQL() {
        throw new SQLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new SQLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public SQLShowColumnsOperator from(Table table) {
        builder = builder.index(Lists.newArrayList(table.getName().format()));
        return self();
    }

    @Override
    public SQLQueryOperator toQueryOperator() {
        throw new SQLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    /**
     * 获取索引映射request
     *
     * @return GetMappingRequest for instance
     */
    public GetMappingRequest getMappingRequest() {
        if (getMappingRequest == null) {
            getMappingRequest = builder.build();
        }
        return getMappingRequest;
    }
}
