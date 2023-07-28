package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.ddl.elasticsearch.ElasticSearchExistIndexOperator;
import cc.allio.uno.data.orm.type.IntegerJavaType;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;

/**
 * {@link SQLCommandType#EXIST_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/30 08:54
 * @since 1.1.4
 */
public class EsExistTableCommandExecutor implements SQLCommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsExistTableCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(SQLOperator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchExistIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the create index operator not ElasticSearchExistIndexOperator, this is %s", operator.getClass().getName()));
        }
        ElasticSearchExistIndexOperator existIndexOperator = (ElasticSearchExistIndexOperator) operator;
        boolean isExist = true;
        // 查询是否存在指定的索引，通过es search方式，当不存在时将会抛出ElasticsearchException
        try {
            elasticsearchClient.search(existIndexOperator.getSearchRequest(), Object.class);
        } catch (ElasticsearchException ex) {
            isExist = false;
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(
                ResultRow.builder()
                        .index(0)
                        .column(SQLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(isExist)
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
