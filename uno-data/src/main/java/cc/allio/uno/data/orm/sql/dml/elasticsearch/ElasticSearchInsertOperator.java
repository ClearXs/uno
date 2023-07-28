package cc.allio.uno.data.orm.sql.dml.elasticsearch;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.sql.PrepareValue;
import cc.allio.uno.data.orm.sql.SQLException;
import cc.allio.uno.data.orm.sql.SQLName;
import cc.allio.uno.data.orm.sql.Table;
import cc.allio.uno.data.orm.sql.dml.SQLInsertOperator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import reactor.util.function.Tuples;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于{@link BulkRequest}做数据创建
 *
 * @author jiangwei
 * @date 2023/5/29 12:13
 * @see BulkOperation
 * @since 1.1.4
 */
public class ElasticSearchInsertOperator implements SQLInsertOperator {

    private BulkRequest bulkRequest;
    private BulkRequest.Builder batchBuilder;
    private final List<BulkOperation> bkps;
    private Table table;

    private static final String ERROR_MSG = "elasticsearch insert operator not support that operator";

    public ElasticSearchInsertOperator() {
        this.batchBuilder = new BulkRequest.Builder();
        this.bkps = Lists.newArrayList();
    }

    @Override
    public String getSQL() {
        BulkRequest request = getBulkRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public SQLInsertOperator parse(String sql) {
        return null;
    }

    @Override
    public void reset() {
        bulkRequest = null;
        batchBuilder = new BulkRequest.Builder();
        bkps.clear();
        table = null;
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
    public SQLInsertOperator from(Table table) {
        this.table = table;
        return self();
    }

    @Override
    public SQLInsertOperator inserts(Map<SQLName, Object> values) {
        return batchInserts(values.keySet(), Collections.singleton(values.values()));
    }

    @Override
    public SQLInsertOperator batchInserts(Collection<SQLName> columns, Collection<Collection<Object>> values) {
        if (table == null) {
            throw new SQLException("ensure invoke #from() given index");
        }
        List<BulkOperation> thisOp = values.stream()
                .map(v ->
                        Streams.zip(columns.stream(),
                                        v.stream(),
                                        (a, b) -> Tuples.of(a.format(), b))
                                .reduce(Maps.newHashMap(),
                                        (m, t) -> {
                                            m.put(t.getT1(), t.getT2());
                                            return m;
                                        },
                                        (m1, m2) -> {
                                            m1.putAll(m2);
                                            return m1;
                                        })
                )
                .map(v -> BulkOperation.of(bp -> bp.create(c -> c.id(IdGenerator.defaultGenerator().getNextIdAsString()).index(table.getName().format()).document(v))))
                .collect(Collectors.toList());
        bkps.addAll(thisOp);
        return self();
    }

    /**
     * 获取批量创建数据{@link BulkRequest}
     *
     * @return BulkRequest
     */
    public synchronized BulkRequest getBulkRequest() {
        if (bulkRequest == null) {
            batchBuilder.operations(bkps);
            bulkRequest = batchBuilder.build();
        }
        return bulkRequest;
    }
}
