package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbQueryOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.result.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * mongodb query command executor
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(QOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbQueryCommandExecutor<R> implements QOInnerCommandExecutor<R, MongodbQueryOperator> {

    final MongoDatabase database;

    public MongodbQueryCommandExecutor(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public List<R> doExec(MongodbQueryOperator operator, ListResultSetHandler<R> handler) throws Throwable {
        // validate
        Table fromColl = operator.getTable();
        Requires.isNotNull(fromColl, "from collection");
        ResultSet resultSet = new ResultSet();
        MongoCollection<Document> collection = database.getCollection(fromColl.getName().format());

        // decide how to use query api
        Bson filter = operator.getFilter();

        // by count
        boolean isCount = operator.isCount();
        if (isCount) {
            long nums = collection.countDocuments(filter);
            ResultRow resultRow = ResultRow.buildCountRow(nums);
            ResultGroup resultGroup = new ResultGroup();
            resultGroup.addRow(resultRow);
            resultSet.setResultGroups(Lists.newArrayList(resultGroup));
        } else {
            // by general query
            FindIterable<Document> documents = collection.find(filter);
            List<ResultGroup> resultGroups =
                    Lists.newArrayList(documents)
                            .stream()
                            .map(document -> {
                                ResultGroup resultGroup = new ResultGroup();
                                for (Map.Entry<String, Object> dEntry : document.entrySet()) {
                                    ResultRow.ResultRowBuilder rowBuilder = ResultRow.builder();
                                    String key = dEntry.getKey();
                                    rowBuilder.column(DSLName.of(key));
                                    // build filed name - value result row
                                    Object value = dEntry.getValue();
                                    if (value != null) {
                                        Object javaValue = toJavaValue(value);
                                        rowBuilder.value(javaValue);
                                    }
                                    ResultRow row = rowBuilder.build();
                                    resultGroup.addRow(row);
                                }
                                return resultGroup;
                            })
                            .toList();
            resultSet.setResultGroups(resultGroups);
        }

        print(log, Map.of("fromColl", fromColl.getName().format(), "filter", filter));
        return handler.apply(resultSet);
    }

    /**
     * change to document value to java value. specific is array and {@link Collection} and {@link Document}
     *
     * @param v the v, not null
     * @return the java value
     */
    public Object toJavaValue(Object v) {
        if (v == null) {
            return null;
        }
        if (Types.isArray(v.getClass())) {
            return Arrays.stream(((Object[]) v)).map(this::toJavaValue).toArray(Object[]::new);
        } else if (v instanceof Collection<?> coll) {
            return coll.stream().map(this::toJavaValue).toList();
        } else if (v instanceof Document doc) {
            Map<String, Object> docMap = Maps.newHashMap();
            for (Map.Entry<String, Object> docEntry : doc.entrySet()) {
                String docKey = docEntry.getKey();
                Object docValue = docEntry.getValue();
                Object javaValue = toJavaValue(docValue);
                docMap.put(docKey, javaValue);
            }
            return docMap;
        }
        return v;
    }
}
