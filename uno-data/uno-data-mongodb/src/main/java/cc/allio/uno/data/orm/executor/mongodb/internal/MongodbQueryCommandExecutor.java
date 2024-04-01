package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.dml.MongodbQueryOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;

/**
 * mongodb query command executor
 *
 * @author j.x
 * @date 2024/3/15 11:34
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
                                    // build filed name - value result row
                                    ResultRow row = ResultRow.builder()
                                            .column(DSLName.of(dEntry.getKey()))
                                            .value(dEntry.getValue())
                                            .build();
                                    resultGroup.addRow(row);
                                }
                                return resultGroup;
                            })
                            .toList();
            resultSet.setResultGroups(resultGroups);
        }

        return handler.apply(resultSet);
    }
}
