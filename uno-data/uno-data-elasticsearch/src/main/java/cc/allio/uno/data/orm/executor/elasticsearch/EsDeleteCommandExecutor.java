package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.ElasticSearchDeleteOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;

/**
 * {@link CommandType#DELETE}
 *
 * @author jiangwei
 * @date 2023/7/4 15:59
 * @since 1.1.4
 */
public class EsDeleteCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsDeleteCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchDeleteOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchDeleteOperator, this is %s", operator.getClass().getName()));
        }
        DeleteByQueryRequest deleteRequest = ((ElasticSearchDeleteOperator) operator).getDeleteRequest();
        DeleteByQueryResponse res = elasticsearchClient.deleteByQuery(deleteRequest);
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_COUNT))
                        .javaType(new IntegerJavaType())
                        .value(res.deleted())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
