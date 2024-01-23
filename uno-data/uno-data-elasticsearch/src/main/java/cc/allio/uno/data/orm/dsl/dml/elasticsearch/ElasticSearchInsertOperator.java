package cc.allio.uno.data.orm.dsl.dml.elasticsearch;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.*;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.json.JsonpUtils;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 基于{@link BulkRequest}做数据创建
 *
 * @author jiangwei
 * @date 2023/5/29 12:13
 * @see BulkOperation
 * @since 1.1.4
 */
@AutoService(InsertOperator.class)
@Operator.Group(OperatorKey.ELASTICSEARCH_LITERAL)
public class ElasticSearchInsertOperator implements InsertOperator {

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
    public String getDSL() {
        BulkRequest request = getBulkRequest();
        String dsl = JsonpUtils.toString(request);
        return dsl.substring(dsl.indexOf(StringPool.COLON + StringPool.SPACE) + 2);
    }

    @Override
    public InsertOperator parse(String dsl) {
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
    public String getPrepareDSL() {
        return null;
    }

    @Override
    public List<PrepareValue> getPrepareValues() {
        throw new DSLException(ERROR_MSG);
    }

    @Override
    public InsertOperator from(Table table) {
        this.table = table;
        return self();
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public InsertOperator inserts(Map<DSLName, Object> values) {
        return batchInserts(values.keySet().stream().toList(), List.of((values.values().stream().toList())));
    }

    @Override
    public InsertOperator batchInserts(List<DSLName> columns, List<List<Object>> values) {
        if (table == null) {
            throw new DSLException("ensure invoke #from() given index");
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
                .toList();
        bkps.addAll(thisOp);
        return self();
    }

    @Override
    public InsertOperator strictFill(String f, Object v) {
        return null;
    }

    @Override
    public InsertOperator strictFill(String f, Supplier<Object> v) {
        return null;
    }

    @Override
    public InsertOperator columns(List<DSLName> columns) {
        return null;
    }

    @Override
    public InsertOperator values(List<Object> values) {
        return null;
    }

    @Override
    public boolean isBatched() {
        return false;
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
