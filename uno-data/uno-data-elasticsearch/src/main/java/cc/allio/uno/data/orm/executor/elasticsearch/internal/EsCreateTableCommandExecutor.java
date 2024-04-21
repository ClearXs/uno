package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.elasticsearch.ddl.EsCreateIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.CTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link CommandType#CREATE_TABLE}
 *
 * @author j.x
 * @date 2023/5/29 21:56
 * @since 1.1.4
 */
@AutoService(CTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsCreateTableCommandExecutor implements CTOInnerCommandExecutor<EsCreateIndexOperator> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsCreateTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean doExec(EsCreateIndexOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        CreateIndexResponse createIndexResponse = indicesClient.create(operator.getCreateIndexRequest());
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(createIndexResponse.acknowledged())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
