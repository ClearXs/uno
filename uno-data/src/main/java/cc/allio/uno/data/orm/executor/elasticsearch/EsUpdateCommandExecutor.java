package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchUpdateOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateByQueryResponse;

/**
 * {@link SQLCommandType#UPDATE}
 *
 * @author jiangwei
 * @date 2023/6/11 20:22
 * @since 1.1.4
 */
public class EsUpdateCommandExecutor implements SQLCommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsUpdateCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(SQLOperator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchUpdateOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the create index operator not ElasticSearchUpdateOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchUpdateOperator updateOperator = (ElasticSearchUpdateOperator) operator;
        UpdateByQueryResponse res = elasticsearchClient.updateByQuery(updateOperator.getUpdateRequest());
        Long updated = res.updated();
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(SQLName.of(BoolResultHandler.GUESS_COUNT))
                        .javaType(new IntegerJavaType())
                        .value(updated)
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
