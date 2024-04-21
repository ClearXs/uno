package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbDropBucketOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.exception.ExecuteException;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import lombok.extern.slf4j.Slf4j;

/**
 * influxdb drop bucket command executor
 *
 * @author j.x
 * @date 2024/4/13 18:45
 * @see InfluxdbDropBucketOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(DTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbDropBucketCommandExecutor implements DTOInnerCommandExecutor<InfluxdbDropBucketOperator> {

    final InfluxDBClient influxDBClient;

    public InfluxdbDropBucketCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public Boolean doExec(InfluxdbDropBucketOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromBucket = operator.getTable();
        Requires.isNotNull(fromBucket, "fromBucket");
        BucketsApi bucketsApi = influxDBClient.getBucketsApi();
        Bucket removalBucket = bucketsApi.findBucketByName(fromBucket.getName().getName());
        if (removalBucket == null) {
            throw new ExecuteException(String.format("From the bucket name %s not found anything bucket", fromBucket.getName().format()));
        }
        ResultRow resultRow;
        try {
            bucketsApi.deleteBucket(removalBucket.getId());
            resultRow = ResultRow.buildUpdateRow(true);
        } catch (Throwable ex) {
            log.error("Failed drop bucket, the bucket is {}", fromBucket, ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
