package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Database;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbShowCollectionsOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.result.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.STInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static cc.allio.uno.data.orm.executor.handler.TableListResultSetHandler.*;

/**
 * mongodb show collections command executor
 *
 * @author j.x
 * @since 1.1.7
 */
@Slf4j
@AutoService(STInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbShowCollectionCommandExecutor implements STInnerCommandExecutor<Table, MongodbShowCollectionsOperator> {

    final MongoClient mongoClient;

    public MongodbShowCollectionCommandExecutor(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public List<Table> doExec(MongodbShowCollectionsOperator operator, ListResultSetHandler<Table> handler) throws Throwable {
        Database fromDb = operator.getFromDb();
        Requires.isNotNull(fromDb, "fromDb");
        List<Table> tables = operator.getTables();
        List<String> filteringCollectionNames = tables.stream().map(Table::getName).map(DSLName::format).toList();
        MongoDatabase database = mongoClient.getDatabase(fromDb.getName().format());
        List<ResultGroup> resultGroups = Lists.newArrayList(database.listCollections())
                .stream()
                .filter(document -> {
                    if (filteringCollectionNames.isEmpty()) {
                        return true;
                    }
                    // filter exist name
                    String collectionName = document.getString("name");
                    return filteringCollectionNames.contains(collectionName);
                })
                .map(document -> {
                    ResultGroup resultGroup = new ResultGroup();
                    // set database name
                    ResultRow rowDb =
                            ResultRow.builder()
                                    .column(TABLE_CATALOG_DSL_NAME)
                                    .value(database.getName())
                                    .build();
                    resultGroup.addRow(rowDb);
                    // set collection name
                    String collectionName = document.getString("name");
                    ResultRow rowName =
                            ResultRow.builder()
                                    .column(TABLE_NAME_DSL_NAME)
                                    .value(collectionName)
                                    .build();
                    resultGroup.addRow(rowName);
                    // set collection type
                    String collectionType = document.getString("type");
                    ResultRow rowType =
                            ResultRow.builder()
                                    .column(TABLE_TYPE_DSL_NAME)
                                    .value(collectionType)
                                    .build();
                    resultGroup.addRow(rowType);
                    return resultGroup;
                })
                .toList();
        print(log, Collections.emptyMap());
        ResultSet resultSet = new ResultSet();
        resultSet.setResultGroups(resultGroups);
        return handler.apply(resultSet);
    }
}
