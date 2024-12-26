package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbDeleteOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.DOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.domain.DeletePredicateRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * influxdb delete command
 *
 * @author j.x
 * @see InfluxdbDeleteOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(DOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbDeleteCommandExecutor implements DOInnerCommandExecutor<InfluxdbDeleteOperator> {

    final InfluxDBClient influxDBClient;
    final InfluxDBClientOptions clientOptions;

    public InfluxdbDeleteCommandExecutor(InfluxDBClient influxDBClient, InfluxDBClientOptions clientOptions) {
        this.influxDBClient = influxDBClient;
        this.clientOptions = clientOptions;
    }

    @Override
    public Boolean doExec(InfluxdbDeleteOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table measurement = operator.getTable();
        if (measurement == null) {
            throw Exceptions.unNull("measurement is empty");
        }
        String bucket = operator.getBucket();
        if (StringUtils.isBlank(bucket)) {
            bucket = clientOptions.getBucket();
        }
        DeleteApi deleteApi = influxDBClient.getDeleteApi();
        String predicate = operator.getDSL();
        Date startTime = operator.getStartTime();
        if (startTime == null) {
            // set to epoch time
            startTime = DateUtil.getEpochTime();
        }
        Date stopTime = operator.getStopTime();
        if (stopTime == null) {
            // set to now
            stopTime = DateUtil.now();
        }
        DeletePredicateRequest deletePredicateRequest = new DeletePredicateRequest();
        deletePredicateRequest.setStart(toOffsetDateTime(startTime));
        deletePredicateRequest.setStop(toOffsetDateTime(stopTime));
        deletePredicateRequest.setPredicate(predicate);
        ResultRow resultRow;
        try {
            deleteApi.delete(deletePredicateRequest, bucket, clientOptions.getOrg());
            resultRow = ResultRow.buildUpdateRow(true);
        } catch (Throwable ex) {
            log.error("Failed to delete predicate [{}], measurement [{}], bucket [{}]", deletePredicateRequest, measurement, bucket, ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        print(log, Map.of("result", resultRow, "measurement", measurement, "predicate", deletePredicateRequest));
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }

    OffsetDateTime toOffsetDateTime(Date date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.toInstant();
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
