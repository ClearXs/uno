package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbExistBucketOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ETOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import lombok.extern.slf4j.Slf4j;

/**
 * influxdb exist bucket command executor
 *
 * @author j.x
 * @date 2024/4/13 19:38
 * @see InfluxdbExistBucketOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(ETOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbExistBucketCommandExecutor implements ETOInnerCommandExecutor<InfluxdbExistBucketOperator> {

    final InfluxDBClient influxDBClient;

    public InfluxdbExistBucketCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public Boolean doExec(InfluxdbExistBucketOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromBucket = operator.getTable();
        Requires.isNotNull(fromBucket, "fromBucket");
        BucketsApi bucketsApi = influxDBClient.getBucketsApi();
        ResultRow resultRow;
        try {
            Bucket existBucket = bucketsApi.findBucketByName(fromBucket.getName().format());
            resultRow = ResultRow.buildUpdateRow(existBucket != null);
        } catch (Throwable ex) {
            log.error("Failed exist bucket {}", fromBucket, ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
