package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.ddl.elasticsearch.ElasticSearchExistIndexOperator;
import cc.allio.uno.data.orm.dsl.type.IntegerJavaType;
import cc.allio.uno.data.orm.executor.handler.BoolResultHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;

/**
 * {@link CommandType#EXIST_TABLE}
 *
 * @author jiangwei
 * @date 2023/5/30 08:54
 * @since 1.1.4
 */
public class EsExistTableCommandExecutor implements CommandTypeExecutor<Boolean> {

    private final ElasticsearchClient elasticsearchClient;

    public EsExistTableCommandExecutor(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public Boolean exec(Operator<?> operator, ResultSetHandler<Boolean> resultSetHandler) throws Throwable {
        if (!ElasticSearchExistIndexOperator.class.isAssignableFrom(operator.getClass())) {
            throw new IllegalArgumentException(String.format("the registry index operator not ElasticSearchExistIndexOperator, this is %s", operator.getClass().getName()));
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
                        .column(DSLName.of(BoolResultHandler.GUESS_UPDATE_OR_UPDATE))
                        .javaType(new IntegerJavaType())
                        .value(isExist)
                        .build());
        return resultSetHandler.apply(resultGroup);
    }
}
