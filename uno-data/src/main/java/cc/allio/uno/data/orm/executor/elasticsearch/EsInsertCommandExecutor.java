package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.dml.elasticsearch.ElasticSearchInsertOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;

/**
 * {@link SQLCommandType#INSERT}
 *
 * @author jiangwei
 * @date 2023/5/30 08:58
 * @since 1.1.4
 */
public class EsInsertCommandExecutor implements SQLCommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsInsertCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(SQLOperator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchInsertOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the create index operator not ElasticSearchCreateIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchInsertOperator insertOperator = (ElasticSearchInsertOperator) operator;
        BulkRequest bulkRequest = insertOperator.getBulkRequest();
        BulkResponse res = elasticsearchClient.bulk(bulkRequest);
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(SQLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(res.errors())
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
