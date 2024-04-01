package cc.allio.uno.data.orm.executor.influxdb;

import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.data.orm.dsl.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.executor.AbstractCommandExecutor;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.influxdb.LogLevel;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;

import java.net.SocketTimeoutException;

/**
 * command executor for time-series database implementation
 *
 * @author j.x
 * @date 2024/4/1 16:38
 * @since 1.1.8
 */
public class InfluxdbCommandExecutor extends AbstractCommandExecutor implements AggregateCommandExecutor {

    private static final String CONNECTION_URL_TEMPLATE = "#{address}";

    private final InfluxDBClient influxDBClient;
    private final InnerCommandExecutorManager manager;
    private final OperatorGroup operatorGroup;

    public InfluxdbCommandExecutor(ExecutorOptions options) {
        super(options);
        InfluxDBClientOptions.Builder clientOptionsBuilder = InfluxDBClientOptions.builder();
        // address
        String address = options.getAddress();
        String connectionString = ExpressionTemplate.parse(CONNECTION_URL_TEMPLATE, "address", address);
        clientOptionsBuilder.connectionString(connectionString);
        // organization
        String organization = options.getDatabase();
        clientOptionsBuilder.org(organization);
        // auth
        String username = options.getUsername();
        String password = options.getPassword();
        clientOptionsBuilder.authenticate(username, password.toCharArray());
        clientOptionsBuilder.logLevel(LogLevel.BASIC);
        InfluxDBClientOptions clientOptions = clientOptionsBuilder.build();
        this.influxDBClient = InfluxDBClientFactory.create(clientOptions);
        SPIInnerCommandScanner scanner = options.getScanner();
        this.manager = scanner.scan(influxDBClient);
        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.INFLUXDB);
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        try {
            return influxDBClient.ping();
        } catch (Throwable ex) {
            throw new SocketTimeoutException(ex.getMessage());
        }
    }

    @Override
    public ExecutorKey getKey() {
        return ExecutorKey.INFLUXDB;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return operatorGroup;
    }

    @Override
    public void destroy() {
        influxDBClient.close();
    }

    @Override
    protected InnerCommandExecutorManager getManager() {
        return manager;
    }
}
