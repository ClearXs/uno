package cc.allio.uno.data.orm.executor.influxdb;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.data.orm.config.influxdb.InfluxdbProperties;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.InfluxDbTableAcceptor;
import cc.allio.uno.data.orm.executor.AbstractCommandExecutor;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.influxdb.internal.InfluxdbCommandExecutorAdaptation;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.influxdb.LogLevel;
import com.influxdb.client.*;
import com.influxdb.client.domain.WritePrecision;

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
        // auth
        String username = options.getUsername();
        String password = options.getPassword();
        clientOptionsBuilder.authenticate(username, password.toCharArray());
        // bucket
        String bucket = options.getDatabase();
        clientOptionsBuilder.bucket(bucket);
        // organization
        String organization = options.get(InfluxdbProperties.ORGANIZATION, String.class).orElse(StringPool.EMPTY);
        clientOptionsBuilder.org(organization);
        // token
        String token = options.get(InfluxdbProperties.TOKEN, String.class).orElse(StringPool.EMPTY);
        clientOptionsBuilder.authenticateToken(token.toCharArray());
        // settings
        LogLevel logLevel = options.get(InfluxdbProperties.LOG_LEVEL, LogLevel.class).orElse(LogLevel.BASIC);
        clientOptionsBuilder.logLevel(logLevel);
        WritePrecision writePrecision = options.get(InfluxdbProperties.WRITE_PRECISION, WritePrecision.class).orElse(WritePrecision.S);
        clientOptionsBuilder.precision(writePrecision);
        InfluxDBClientOptions clientOptions = clientOptionsBuilder.build();
        this.influxDBClient = InfluxDBClientFactory.create(clientOptions);
        SPIInnerCommandScanner scanner = options.getScanner();
        InfluxdbCommandExecutorAdaptation adaptation = new InfluxdbCommandExecutorAdaptation(influxDBClient, clientOptions);
        this.manager = scanner.scan(influxDBClient, clientOptions, adaptation);

        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.INFLUXDB, options);
        options.customizeMetaAcceptorSetter(Table.class, new InfluxDbTableAcceptor());
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
    protected InnerCommandExecutorManager getInnerCommandExecutorManager() {
        return manager;
    }
}
