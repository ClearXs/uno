package cc.allio.uno.data.orm.executor.mongodb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Database;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.mongodb.ddl.MongodbShowCollectionsOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.STInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.List;

/**
 * mongodb show collections command executor
 *
 * @author j.x
 * @date 2024/3/15 11:23
 * @since 1.1.7
 */
@Slf4j
@AutoService(STInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.MONGODB_LITERAL)
public class MongodbShowCollectionCommandExecutor implements STInnerCommandExecutor<MongodbShowCollectionsOperator> {

    final MongoClient mongoClient;

    public MongodbShowCollectionCommandExecutor(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public List<Table> doExec(MongodbShowCollectionsOperator operator, ListResultSetHandler<Table> handler) throws Throwable {
        Database fromDb = operator.getFromDb();
        Requires.isNotNull(fromDb, "fromDb");
        List<Table> tables = operator.getTables();
        MongoDatabase database = mongoClient.getDatabase(fromDb.getName().format());
        for (Document document : database.listCollections()) {
        }


        return null;
    }
}
