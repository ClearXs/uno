package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.ElasticSearchDropIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link CommandType#DELETE_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/30 08:53
 * @since 1.1.4
 */
public class EsDeleteTableCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsDeleteTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchDropIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchExistIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchDropIndexOperator dropIndexOperator = (ElasticSearchDropIndexOperator) operator;
        DeleteIndexResponse res = indicesClient.delete(dropIndexOperator.getDeleteIndexRequest());
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
