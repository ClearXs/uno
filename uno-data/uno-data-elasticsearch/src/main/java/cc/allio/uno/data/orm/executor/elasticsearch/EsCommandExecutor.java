package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.dsl.exception.DSLException;
import cc.allio.uno.data.orm.executor.*;
import cc.allio.uno.data.orm.dsl.*;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;
import cc.allio.uno.data.orm.executor.internal.InnerCommandExecutorManager;
import cc.allio.uno.data.orm.executor.internal.SPIInnerCommandScanner;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * 基于es的SQL执行器
 *
 * @author j.x
 * @date 2023/4/19 11:45
 * @since 1.1.4
 */
@Slf4j
public class EsCommandExecutor extends AbstractCommandExecutor implements AggregateCommandExecutor {

    private final OperatorGroup operatorGroup;
    private final RestClient restClient;
    private final InnerCommandExecutorManager manager;

    public EsCommandExecutor(ExecutorOptions options, RestClientBuilder restClientBuilder) {
        super(options);
        this.restClient = restClientBuilder.build();
        JsonpMapper mapper = new JacksonJsonpMapper();
        RestClientTransport transport = new RestClientTransport(restClient, mapper);
        this.operatorGroup = OperatorGroup.getOperatorGroup(OperatorKey.ELASTICSEARCH);
        SPIInnerCommandScanner scanner = options.getScanner();
        this.manager = scanner.scan(new ElasticsearchIndicesClient(transport), new ElasticsearchClient(transport));
    }

    @Override
    public List<ColumnDef> showColumns(ShowColumnsOperator operator) {
        try {
            return manager.getShowColumn().exec(operator, getOptions().obtainColumnDefListResultSetHandler());
        } catch (Throwable ex) {
            throw new DSLException("exec show columns has err", ex);
        }
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        return restClient.isRunning();
    }

    @Override
    public ExecutorKey getKey() {
        return ExecutorKey.ELASTICSEARCH;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return operatorGroup;
    }

    @Override
    public void destroy() {
        try {
            restClient.close();
        } catch (IOException ex) {
            throw new DSLException(ex);
        }
    }

    @Override
    protected InnerCommandExecutorManager getManager() {
        return manager;
    }
}
