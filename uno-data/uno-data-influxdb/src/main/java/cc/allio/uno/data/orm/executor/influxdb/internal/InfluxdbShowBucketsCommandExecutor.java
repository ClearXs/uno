package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.ddl.InfluxdbShowBucketsOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.STInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.*;
import com.influxdb.client.domain.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cc.allio.uno.data.orm.executor.handler.TableListResultSetHandler.*;

/**
 * influxdb show buckets command executor
 *
 * @author j.x
 * @see InfluxdbShowBucketsOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(STInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbShowBucketsCommandExecutor implements STInnerCommandExecutor<Table, InfluxdbShowBucketsOperator> {

    final InfluxDBClient influxDBClient;

    public InfluxdbShowBucketsCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public List<Table> doExec(InfluxdbShowBucketsOperator operator, ListResultSetHandler<Table> handler) throws Throwable {
        List<Table> fromBuckets = operator.getFromBuckets();
        if (fromBuckets == null) {
            fromBuckets = Collections.emptyList();
        }
        List<String> filteringBucketNames = fromBuckets.stream().map(Table::getName).map(DSLName::format).toList();
        BucketsApi bucketsApi = influxDBClient.getBucketsApi();
        List<Bucket> buckets = bucketsApi.findBuckets();
        List<ResultGroup> resultGroups =
                buckets.stream()
                        .filter(bucket -> {
                            if (filteringBucketNames.isEmpty()) {
                                return true;
                            }
                            // filter exist name
                            String bucketName = bucket.getName();
                            return filteringBucketNames.contains(bucketName);
                        })
                        .map(bucket -> {
                            ResultGroup resultGroup = new ResultGroup();
                            // set database name
                            ResultRow rowDb =
                                    ResultRow.builder()
                                            .column(TABLE_CATALOG_DSL_NAME)
                                            .value(bucket.getName())
                                            .build();
                            resultGroup.addRow(rowDb);
                            // set bucket name
                            String bucketName = bucket.getName();
                            ResultRow rowName =
                                    ResultRow.builder()
                                            .column(TABLE_NAME_DSL_NAME)
                                            .value(bucketName)
                                            .build();
                            resultGroup.addRow(rowName);
                            // set bucket type
                            String bucketType = bucket.getType().getValue();
                            ResultRow rowType =
                                    ResultRow.builder()
                                            .column(TABLE_TYPE_DSL_NAME)
                                            .value(bucketType)
                                            .build();
                            resultGroup.addRow(rowType);
                            return resultGroup;
                        })
                        .toList();

        print(log, Map.of("buckets", buckets));
        ResultSet resultSet = new ResultSet();
        resultSet.setResultGroups(resultGroups);
        return handler.apply(resultSet);
    }
}
