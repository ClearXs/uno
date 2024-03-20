package cc.allio.uno.data.orm.dsl.ddl.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.exception.DDLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;

/**
 * 删除索引{@link DeleteIndexRequest}
 *
 * @author j.x
 * @date 2023/5/29 18:43
 * @since 1.1.4
 */
@AutoService(DropTableOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsDropIndexOperator implements DropTableOperator {

    private DBType dbType;
    private DeleteIndexRequest deleteIndexRequest;
    private DeleteIndexRequest.Builder builder;
    private Table table;
    private static final String ERROR_MSG = "elasticsearch drop operator not support that operator";

    public EsDropIndexOperator() {
        this.dbType = DBType.ELASTIC_SEARCH;
        this.builder = new DeleteIndexRequest.Builder();
    }

    @Override
    public String getDSL() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public DropTableOperator parse(String dsl) {
        return null;
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
    public DropTableOperator from(Table table) {
        this.table = table;
        builder.index(table.getName().format());
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public DropTableOperator ifExist(Boolean ifExist) {
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
