package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.elasticsearch.ddl.EsExistIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ETOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;

/**
 * {@link CommandType#EXIST_TABLE}
 *
 * @author j.x
 * @date 2023/5/30 08:54
 * @since 1.1.4
 */
@AutoService(ETOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.ELASTICSEARCH_LITERAL)
public class EsExistTableCommandExecutor implements ETOInnerCommandExecutor<EsExistIndexOperator> {

    private final ElasticsearchClient elasticsearchClient;

    public EsExistTableCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean doExec(EsExistIndexOperator operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        boolean isExist = true;
        // 查询是否存在指定的索引，通过es search方式，当不存在时将会抛出ElasticsearchException
        try {
            elasticsearchClient.search(operator.getSearchRequest(), Object.class);
        } catch (ElasticsearchException ex) {
            isExist = false;
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(isExist)
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
