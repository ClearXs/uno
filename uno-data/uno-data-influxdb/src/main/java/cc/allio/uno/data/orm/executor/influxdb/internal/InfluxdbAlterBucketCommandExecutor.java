package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbAlterBucketOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.exception.ExecuteException;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ATOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.BucketRetentionRules;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * influxdb alter bucket command executor
 *
 * @author j.x
 * @see InfluxdbAlterBucketOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(ATOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbAlterBucketCommandExecutor implements ATOInnerCommandExecutor<InfluxdbAlterBucketOperator> {

    final InfluxDBClient influxDBClient;

    public InfluxdbAlterBucketCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public Boolean doExec(InfluxdbAlterBucketOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromBucket = operator.getFromBucket();
        Table toBucket = operator.getToBucket();
        Requires.isNotNull(fromBucket, "fromBucket");
        Requires.isNotNull(toBucket, "toBucket");
        BucketsApi bucketsApi = influxDBClient.getBucketsApi();
        Bucket removalBucket = bucketsApi.findBucketByName(fromBucket.getName().getName());
        if (removalBucket == null) {
            throw new ExecuteException(String.format("From the bucket name %s not found anything bucket", fromBucket.getName().format()));
        }
        Bucket updateBucket = removalBucket;
        updateBucket.setName(toBucket.getName().format());
        List<BucketRetentionRules> retentionRules = operator.getRetentionRules();
        if (retentionRules != null) {
            updateBucket.setRetentionRules(retentionRules);
        }
        ResultRow resultRow;
        try {
            bucketsApi.updateBucket(updateBucket);
            resultRow = ResultRow.buildUpdateRow(true);
        } catch (Throwable ex) {
            log.error("Failed to update bucket. ", ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
