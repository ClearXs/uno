package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.elasticsearch.dml.EsDeleteOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;

/**
 * {@link CommandType#DELETE}
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(DOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsDeleteCommandExecutor implements DOInnerCommandExecutor<EsDeleteOperator> {

    private final ElasticsearchClient elasticsearchClient;

    public EsDeleteCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean doExec(EsDeleteOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        DeleteByQueryRequest deleteRequest = operator.getDeleteRequest();
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
