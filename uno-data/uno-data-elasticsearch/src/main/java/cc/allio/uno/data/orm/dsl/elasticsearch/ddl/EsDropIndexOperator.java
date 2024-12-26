package cc.allio.uno.data.orm.dsl.elasticsearch.ddl;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.exception.DDLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;

import java.util.function.UnaryOperator;

/**
 * 删除索引{@link DeleteIndexRequest}
 *
 * @author j.x
 * @since 1.1.4
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsDropIndexOperator implements DropTableOperator<EsDropIndexOperator> {

    private final DBType dbType;
    private DeleteIndexRequest deleteIndexRequest;
    private DeleteIndexRequest.Builder builder;
    private Table table;
    private static final String ERROR_MSG = "elasticsearch drop operator not support that operator";

    public EsDropIndexOperator() {
        this.dbType = DBType.ELASTICSEARCH;
        this.builder = new DeleteIndexRequest.Builder();
    }

    @Override
    public String getDSL() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public EsDropIndexOperator parse(String dsl) {
        return null;
    }

    @Override
    public EsDropIndexOperator customize(UnaryOperator<EsDropIndexOperator> operatorFunc) {
        return operatorFunc.apply(new EsDropIndexOperator());
    }

    @Override
    public void reset() {
        deleteIndexRequest = null;
        builder = new DeleteIndexRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        throw new DDLException("setDBType");
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public EsDropIndexOperator from(Table table) {
        this.table = table;
        builder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public EsDropIndexOperator ifExist(Boolean ifExist) {
        throw new DSLException(ERROR_MSG);
    }

    /**
     * 获取删除index请求
     *
     * @return DeleteIndexRequest
     */
    public DeleteIndexRequest getDeleteIndexRequest() {
        if (deleteIndexRequest == null) {
            deleteIndexRequest = builder.build();
        }
        return deleteIndexRequest;
    }
}
