package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.executor.AbstractCommandExecutor;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.net.SocketTimeoutException;
import java.util.List;

/**
 * command executor for document database mongodb implementation
 *
 * @author j.x
 * @date 2024/3/10 23:32
 * @since 1.1.7
 */
public class MongodbCommandExecutor extends AbstractCommandExecutor implements AggregateCommandExecutor {

    private static final String MONGO_CONNECTION_TEMPLATE = "mongodb://#{username}:#{password}@#{address}/?retryWrites=true&w=majority";
    private static final ExpressionTemplate TEMPLATE_PARSER = ExpressionTemplate.defaultTemplate();

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final OperatorGroup operatorGroup;

    private final InnerCommandExecutorManager manager;

    public MongodbCommandExecutor(ExecutorOptions options) {
        super(options);
        String username = options.getUsername();
        String password = options.getPassword();
        String address = options.getAddress();
        String url = TEMPLATE_PARSER.parseTemplate(MONGO_CONNECTION_TEMPLATE, "username", username, "password", password, "address", address);
        String databaseName = options.getDatabase();
        this.mongoClient = MongoClients.create(url);
        this.database = mongoClient.getDatabase(databaseName);
        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.ELASTICSEARCH);
        SPIInnerCommandScanner scanner = options.getScanner();
        this.manager = scanner.scan(database);
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        return true;
    }

    @Override
    public ExecutorKey getKey() {
        return ExecutorKey.MONGODB;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return operatorGroup;
    }

    @Override
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    protected InnerCommandExecutorManager getManager() {
        return manager;
    }
}
