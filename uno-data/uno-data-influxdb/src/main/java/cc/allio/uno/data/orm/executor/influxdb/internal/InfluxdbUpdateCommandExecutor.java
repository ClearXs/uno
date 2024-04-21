package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbInsertOperator;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbUpdateOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.UOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.InfluxDBClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * influxdb update command (bridge to {@link InfluxdbInsertCommandExecutor})
 *
 * @author j.x
 * @date 2024/4/14 17:45
 * @see InfluxdbUpdateOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(UOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbUpdateCommandExecutor implements UOInnerCommandExecutor<InfluxdbUpdateOperator> {

    final InfluxDBClient influxDBClient;

    final InfluxdbInsertCommandExecutor bridge;

    public InfluxdbUpdateCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
        this.bridge = new InfluxdbInsertCommandExecutor(influxDBClient);
    }

    @Override
    public Boolean doExec(InfluxdbUpdateOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        InfluxdbInsertOperator influxdbInsertOperator = new InfluxdbInsertOperator();
        influxdbInsertOperator.from(operator.getTable());
        List<Map<DSLName, Object>> tags = influxdbInsertOperator.getTags();
        for (Map<DSLName, Object> tag : tags) {
            influxdbInsertOperator.tags(tag);
        }
        List<Map<DSLName, Object>> fields = influxdbInsertOperator.getFields();
        for (Map<DSLName, Object> field : fields) {
            influxdbInsertOperator.inserts(field);
        }
        return bridge.doExec(influxdbInsertOperator, handler);
    }
}
