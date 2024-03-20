package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.EsInsertOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.IOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;

/**
 * {@link CommandType#INSERT}
 *
 * @author j.x
 * @date 2023/5/30 08:58
 * @since 1.1.4
 */
@AutoService(IOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsInsertCommandExecutor implements IOInnerCommandExecutor<EsInsertOperator> {

    private final ElasticsearchClient elasticsearchClient;

    public EsInsertCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean doExec(EsInsertOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        BulkRequest bulkRequest = operator.getBulkRequest();
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
