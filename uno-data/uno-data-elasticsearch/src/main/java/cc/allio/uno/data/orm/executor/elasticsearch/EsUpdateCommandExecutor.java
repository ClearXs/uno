package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.elasticsearch.ElasticSearchUpdateOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;

/**
 * {@link CommandType#UPDATE}
 *
 * @author jiangwei
 * @date 2023/6/11 20:22
 * @since 1.1.4
 */
public class EsUpdateCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsUpdateCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchUpdateOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchUpdateOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchUpdateOperator updateOperator = (ElasticSearchUpdateOperator) operator;
        UpdateByQueryResponse res = elasticsearchClient.updateByQuery(updateOperator.getUpdateRequest());
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
