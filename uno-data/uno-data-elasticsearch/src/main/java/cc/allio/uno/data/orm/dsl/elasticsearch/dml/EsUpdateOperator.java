package cc.allio.uno.data.orm.dsl.elasticsearch.dml;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
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
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * 基于{@link UpdateByQueryRequest}做数据更新
 *
 * @author j.x
 * @date 2023/5/29 13:03
 * @since 1.1.4
 */
@AutoService(UpdateOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class EsUpdateOperator extends EsWhereOperatorImpl<EsUpdateOperator> implements UpdateOperator<EsUpdateOperator> {

    private final DBType dbType;
    private UpdateByQueryRequest updateRequest;
    private UpdateByQueryRequest.Builder updateBuilder;
    private Table table;

    public EsUpdateOperator() {
        super();
        this.dbType = DBType.ELASTICSEARCH;
        this.updateBuilder = new UpdateByQueryRequest.Builder();
    }

    @Override
    public String getDSL() {
        UpdateByQueryRequest request = getUpdateRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public EsUpdateOperator parse(String dsl) {
        // nothing todo
        return self();
    }

    @Override
    public EsUpdateOperator customize(UnaryOperator<EsUpdateOperator> operatorFunc) {
        return operatorFunc.apply(new EsUpdateOperator());
    }

    @Override
    public void reset() {
        super.reset();
        this.updateBuilder = new UpdateByQueryRequest.Builder();
    }

    @Override
    public void setDBType(DBType dbType) {
        // nothing todo
    }

    @Override
    public DBType getDBType() {
        return dbType;
    }

    @Override
    public String getPrepareDSL() {
        return getDSL();
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        return List.of();
    }

    @Override
    public EsUpdateOperator from(Table table) {
        this.table = table;
        this.updateBuilder = updateBuilder.index(Collections.singletonList(table.getName().format()));
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public EsUpdateOperator updates(Map<DSLName, Object> values) {
        // source
        String source = values.keySet().stream().map(o -> "ctx._source['" + o.format() + "'] = params['" + o.format() + "']").collect(Collectors.joining(StringPool.SEMICOLON));
        // params
        Map<String, JsonData> params = values.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().format(), v -> JsonData.of(v.getValue())));
        this.updateBuilder = updateBuilder.script(s -> s.inline(i -> i.params(params).source(source)));
        return self();
    }

    @Override
    public EsUpdateOperator strictFill(String f, Supplier<Object> v) {
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
}