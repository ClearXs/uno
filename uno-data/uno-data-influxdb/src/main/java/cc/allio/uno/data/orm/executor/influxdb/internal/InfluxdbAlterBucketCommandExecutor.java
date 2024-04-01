package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbAlterBucketOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.exception.ExecuteException;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.ATOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.influxdb.client.BucketsApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.BucketRetentionRules;
import lombok.extern.slf4j.Slf4j;

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
        Long retention = operator.getRetention();
        if (retention != null) {
            BucketRetentionRules retentionRules = new BucketRetentionRules();
            retentionRules.setShardGroupDurationSeconds(retention);
            retentionRules.setEverySeconds(Math.toIntExact(retention));
            updateBucket.setRetentionRules(Lists.newArrayList(retentionRules));
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
