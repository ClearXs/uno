package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchDropIndexOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link SQLCommandType#DELETE_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/30 08:53
 * @since 1.1.4
 */
public class EsDeleteTableCommandExecutor implements SQLCommandTypeExecutor<Boolean> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsDeleteTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean exec(SQLOperator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchDropIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the create index operator not ElasticSearchExistIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchDropIndexOperator dropIndexOperator = (ElasticSearchDropIndexOperator) operator;
        DeleteIndexResponse res = indicesClient.delete(dropIndexOperator.getDeleteIndexRequest());
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(SQLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(res.acknowledged())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
