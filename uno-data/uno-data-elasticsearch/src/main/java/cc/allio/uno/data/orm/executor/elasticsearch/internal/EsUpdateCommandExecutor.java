package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.EsUpdateOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.UOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;

/**
 * {@link CommandType#UPDATE}
 *
 * @author j.x
 * @date 2023/6/11 20:22
 * @since 1.1.4
 */
@AutoService(UOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsUpdateCommandExecutor implements UOInnerCommandExecutor<EsUpdateOperator> {

    private final ElasticsearchClient elasticsearchClient;

    public EsUpdateCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean doExec(EsUpdateOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        UpdateByQueryResponse res = elasticsearchClient.updateByQuery(operator.getUpdateRequest());
        Long updated = res.updated();
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_COUNT))
                        .javaType(new IntegerJavaType())
                        .value(updated)
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
