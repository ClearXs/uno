package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.EsDropIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link CommandType#DELETE_TABLE}
 *
 * @author j.x
 * @date 2023/5/30 08:53
 * @since 1.1.4
 */
@AutoService(DTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsDeleteTableCommandExecutor implements DTOInnerCommandExecutor<EsDropIndexOperator> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsDeleteTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean doExec(EsDropIndexOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        DeleteIndexResponse res = indicesClient.delete(operator.getDeleteIndexRequest());
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(res.acknowledged())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
