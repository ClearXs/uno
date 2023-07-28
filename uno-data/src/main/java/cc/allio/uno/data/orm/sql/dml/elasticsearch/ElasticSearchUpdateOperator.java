package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.UpdateByQueryRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.dml.SQLUpdateOperator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于{@link UpdateByQueryRequest}做数据更新
 *
 * @author jiangwei
 * @date 2023/5/29 13:03
 * @since 1.1.4
 */
public class ElasticSearchUpdateOperator extends ElasticSearchGenericWhereOperator<SQLUpdateOperator> implements SQLUpdateOperator {

    private UpdateByQueryRequest updateRequest;
    private UpdateByQueryRequest.Builder updateBuilder;
    private static final String ERROR_MSG = "elasticsearch update operator not support that operator";

    public ElasticSearchUpdateOperator() {
        super();
        this.updateBuilder = new UpdateByQueryRequest.Builder();
    }

    @Override
    public String getSQL() {
        UpdateByQueryRequest request = getUpdateRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public SQLUpdateOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        updateRequest = null;
        updateBuilder = new UpdateByQueryRequest.Builder();
    }

    @Override
    public String getPrepareSQL() {
        return null;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new SQLException(ERROR_MSG);
    }

    @Override
    public SQLUpdateOperator from(Table table) {
        this.updateBuilder = updateBuilder.index(Collections.singletonList(table.getName().format()));
        return self();
    }

    @Override
    public SQLUpdateOperator updates(Map<SQLName, Object> values) {
        // source
        String source = values.keySet().stream().map(o -> "ctx._source['" + o.format() + "'] = params['" + o.format() + "']").collect(Collectors.joining(StringPool.SEMICOLON));
        // params
        Map<String, JsonData> params = values.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().format(), v -> JsonData.of(v.getValue())));
        this.updateBuilder = updateBuilder.script(s -> s.inline(i -> i.params(params).source(source)));
        return self();
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
