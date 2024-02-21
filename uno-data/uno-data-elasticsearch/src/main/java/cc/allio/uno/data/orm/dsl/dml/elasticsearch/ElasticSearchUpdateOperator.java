package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.dsl.type.DBType;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 基于{@link UpdateByQueryRequest}做数据更新
 *
 * @author jiangwei
 * @date 2023/5/29 13:03
 * @since 1.1.4
 */
@AutoService(UpdateOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class ElasticSearchUpdateOperator extends ElasticSearchGenericWhereOperator<UpdateOperator> implements UpdateOperator {

    private DBType dbType;
    private UpdateByQueryRequest updateRequest;
    private UpdateByQueryRequest.Builder updateBuilder;
    private Table table;
    private static final String ERROR_MSG = "elasticsearch update operator not support that operator";

    public ElasticSearchUpdateOperator() {
        super();
        this.dbType = DBType.ELASTIC_SEARCH;
        this.updateBuilder = new UpdateByQueryRequest.Builder();
    }

    @Override
    public String getDSL() {
        UpdateByQueryRequest request = getUpdateRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public UpdateOperator parse(String dsl) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        this.updateBuilder = new UpdateByQueryRequest.Builder();
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
    public UpdateOperator from(Table table) {
        this.table = table;
        this.updateBuilder = updateBuilder.index(Collections.singletonList(table.getName().format()));
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public UpdateOperator updates(Map<DSLName, Object> values) {
        // source
        String source = values.keySet().stream().map(o -> "ctx._source['" + o.format() + "'] = params['" + o.format() + "']").collect(Collectors.joining(StringPool.SEMICOLON));
        // params
        Map<String, JsonData> params = values.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().format(), v -> JsonData.of(v.getValue())));
        this.updateBuilder = updateBuilder.script(s -> s.inline(i -> i.params(params).source(source)));
        return self();
    }

    @Override
    public UpdateOperator strictFill(String f, Supplier<Object> v) {
        return null;
    }

    /**
     * 获取更新request
     *
     * @return UpdateByQueryRequest
     */
    public UpdateByQueryRequest getUpdateRequest() {
        if (updateRequest == null) {
            Query query = buildQuery();
            updateBuilder.query(query);
            updateRequest = updateBuilder.build();
        }
        return updateRequest;
    }

    @Override
    public UpdateOperator notIn(DSLName sqlName, Object... values) {
        return null;
    }

    @Override
    public UpdateOperator notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public UpdateOperator $notLike(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public UpdateOperator notLike$(DSLName sqlName, Object value) {
        return null;
    }

    @Override
    public UpdateOperator $notLike$(DSLName sqlName, Object value) {
        return null;
    }
}
