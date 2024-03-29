package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.dml.DeleteOperator;

import java.util.Collections;
import java.util.List;

/**
 * {@link DeleteByQueryRequest}实现删除数据
 *
 * @author j.x
 * @date 2023/5/29 13:49
 * @since 1.1.4
 */
@AutoService(DeleteOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsDeleteOperator extends EsGenericWhereOperator<DeleteOperator> implements DeleteOperator {

    private DBType dbType;
    private DeleteByQueryRequest deleteRequest;
    private DeleteByQueryRequest.Builder deleteBuilder;
    private Table table;
    private static final String ERROR_MSG = "elasticsearch delete operator not support that operator";

    public EsDeleteOperator() {
        this.dbType = DBType.ELASTIC_SEARCH;
        this.deleteBuilder = new DeleteByQueryRequest.Builder();
    }

    @Override
    public String getDSL() {
        DeleteByQueryRequest request = getDeleteRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public DeleteOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.deleteRequest = null;
        this.deleteBuilder = new DeleteByQueryRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        throw Exceptions.unOperate("setDBType");
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        return null;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public DeleteOperator from(Table table) {
        this.table = table;
        deleteBuilder.index(Collections.singletonList(table.getName().format()));
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    /**
     * 获取{@link DeleteByQueryRequest}实例
     *
     * @return DeleteByQueryRequest for instance
     */
    public DeleteByQueryRequest getDeleteRequest() {
        if (deleteRequest == null) {
            Query query = buildQuery();
            deleteRequest = deleteBuilder.query(query).build();
        }
        return deleteRequest;
    }

    @Override
    public DeleteOperator neq(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public DeleteOperator notIn(DSLName sqlName, Object... values) {
        return null;
    }

    @Override
    public DeleteOperator notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public DeleteOperator $notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public DeleteOperator notLike$(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public DeleteOperator $notLike$(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public DeleteOperator nor() {
        return null;
    }
}
