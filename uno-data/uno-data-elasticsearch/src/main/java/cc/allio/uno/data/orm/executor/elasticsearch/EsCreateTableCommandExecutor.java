package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.ElasticSearchCreateIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link CommandType#CREATE_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/29 21:56
 * @since 1.1.4
 */
public class EsCreateTableCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsCreateTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchCreateIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchCreateIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchCreateIndexOperator indexOperator = ((ElasticSearchCreateIndexOperator) operator);
        CreateIndexResponse createIndexResponse = indicesClient.create(indexOperator.getCreateIndexRequest());
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
