package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * es 查询所有映射.{@link GetMappingRequest}
 *
 * @author jiangwei
 * @date 2023/6/11 20:07
 * @since 1.1.4
 */
@AutoService(ShowColumnsOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class ElasticSearchShowColumnsOperator implements ShowColumnsOperator {
    private GetMappingRequest.Builder builder;
    private GetMappingRequest getMappingRequest;
    private Table table;

    public ElasticSearchShowColumnsOperator() {
        this.builder = new GetMappingRequest.Builder();
    }

    @Override
    public String getDSL() {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public ShowColumnsOperator parse(String dsl) {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public void reset() {
        builder = new GetMappingRequest.Builder();
        getMappingRequest = null;
    }

    @Override
    public String getPrepareDSL() {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
    }

    @Override
    public ShowColumnsOperator from(Table table) {
        this.table = table;
        builder = builder.index(Lists.newArrayList(table.getName().format()));
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public QueryOperator toQueryOperator() {
        throw new DSLException(String.format("%s This operation is not supported", this.getClass().getName()));
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
