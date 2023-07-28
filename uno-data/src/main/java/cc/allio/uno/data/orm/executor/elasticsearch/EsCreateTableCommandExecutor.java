package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchCreateIndexOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;

/**
 * {@link SQLCommandType#CREATE_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/29 21:56
 * @since 1.1.4
 */
public class EsCreateTableCommandExecutor implements SQLCommandTypeExecutor<Boolean> {

    private final ElasticsearchIndicesClient indicesClient;

    public EsCreateTableCommandExecutor(ElasticsearchIndicesClient indicesClient) {
        this.indicesClient = indicesClient;
    }

    @Override
    public Boolean exec(SQLOperator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchCreateIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the create index operator not ElasticSearchCreateIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchCreateIndexOperator indexOperator = ((ElasticSearchCreateIndexOperator) operator);
        CreateIndexResponse createIndexResponse = indicesClient.create(indexOperator.getCreateIndexRequest());
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(SQLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(createIndexResponse.acknowledged())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
