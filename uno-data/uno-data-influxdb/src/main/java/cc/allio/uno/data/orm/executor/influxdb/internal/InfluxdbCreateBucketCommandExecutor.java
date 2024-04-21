package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.Requires;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.core.util.Values;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbCreateBucketOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.CTOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.*;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.Organization;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * influxdb create bucket command executor
 *
 * @author j.x
 * @date 2024/4/13 18:25
 * @see InfluxdbCreateBucketOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(CTOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbCreateBucketCommandExecutor implements CTOInnerCommandExecutor<InfluxdbCreateBucketOperator> {

    final InfluxDBClient influxDBClient;
    final InfluxdbCommandExecutorAdaptation adaptation;

    public InfluxdbCreateBucketCommandExecutor(InfluxDBClient influxDBClient, InfluxdbCommandExecutorAdaptation adaptation) {
        this.influxDBClient = influxDBClient;
        this.adaptation = adaptation;
    }

    @Override
    public Boolean doExec(InfluxdbCreateBucketOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table fromBucket = operator.getFromBucket();
        Requires.isNotNull(fromBucket, "fromBucket");
        BucketsApi bucketsApi = influxDBClient.getBucketsApi();
        // create bucket
        Bucket bucket = new Bucket();
        bucket.setName(fromBucket.getName().format());
        Values.mapping(operator::getDescription, bucket::setDescription);
        Values.mapping(operator::getLabels, bucket::setLabels);
        Values.mapping(operator::getRetentionRules, bucket::setRetentionRules);
        Values.mapping(operator::getSchemaType, bucket::setSchemaType);
        // set org
        Organization currentOrganization = adaptation.getCurrentOrganization();
        Values.mapping(currentOrganization::getId, bucket::setOrgID);
        ResultRow resultRow;
        try {
            Bucket createBucket = bucketsApi.createBucket(bucket);
            String id = createBucket.getId();
            resultRow = ResultRow.buildUpdateRow(StringUtils.isNotBlank(id));
        } catch (Throwable ex) {
            log.error("Failed to Create influxdb bucket {}", fromBucket, ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
