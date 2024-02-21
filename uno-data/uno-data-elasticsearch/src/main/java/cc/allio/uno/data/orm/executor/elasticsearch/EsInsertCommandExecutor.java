package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.ElasticSearchInsertOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;

/**
 * {@link CommandType#INSERT}
 *
 * @author jiangwei
 * @date 2023/5/30 08:58
 * @since 1.1.4
 */
public class EsInsertCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsInsertCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchInsertOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchCreateIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchInsertOperator insertOperator = (ElasticSearchInsertOperator) operator;
        BulkRequest bulkRequest = insertOperator.getBulkRequest();
        BulkResponse res = elasticsearchClient.bulk(bulkRequest);
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(res.errors())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
