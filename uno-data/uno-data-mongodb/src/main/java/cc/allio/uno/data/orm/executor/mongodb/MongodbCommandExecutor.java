package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.data.orm.dsl.opeartorgroup.Operators;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.AbstractCommandExecutor;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.net.SocketTimeoutException;

/**
 * command executor for document database mongodb implementation
 *
 * @author j.x
 * @date 2024/3/10 23:32
 * @since 1.1.7
 */
public class MongodbCommandExecutor extends AbstractCommandExecutor implements AggregateCommandExecutor {

    private static final String MONGO_CONNECTION_TEMPLATE = "mongodb://#{username}:#{password}@#{address}/?retryWrites=true&w=majority&directConnection=true&serverSelectionTimeoutMS=2000";
    private static final String NO_AUTH_MONGO_CONNECTION_TEMPLATE = "mongodb://#{address}/?retryWrites=true&w=majority&directConnection=true&serverSelectionTimeoutMS=2000";
    private static final ExpressionTemplate TEMPLATE_PARSER = ExpressionTemplate.defaultTemplate();

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final Operators operatorGroup;

    private final InnerCommandExecutorManager manager;

    public MongodbCommandExecutor(ExecutorOptions options) {
        super(options);
        String username = options.getUsername();
        String password = options.getPassword();
        String address = options.getAddress();
        String url;
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            url = TEMPLATE_PARSER.parseTemplate(NO_AUTH_MONGO_CONNECTION_TEMPLATE, "address", address);
        } else {
            url = TEMPLATE_PARSER.parseTemplate(MONGO_CONNECTION_TEMPLATE, "username", username, "password", password, "address", address);
        }
        String databaseName = options.getDatabase();
        this.mongoClient = MongoClients.create(url);
        this.database = mongoClient.getDatabase(databaseName);
        this.operatorGroup = Operators.getOperatorGroup(OperatorKey.MONGODB, options);
        SPIInnerCommandScanner scanner = options.getScanner();
        this.manager = scanner.scan(database, mongoClient);
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
    public Operators getOperatorGroup() {
        return operatorGroup;
    }

    @Override
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    protected InnerCommandExecutorManager getInnerCommandExecutorManager() {
        return manager;
    }
}
