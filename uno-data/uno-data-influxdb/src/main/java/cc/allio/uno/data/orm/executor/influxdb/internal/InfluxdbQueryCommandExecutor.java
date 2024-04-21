package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbQueryOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultRow;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.QOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.google.common.collect.Lists;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.InfluxQLQueryApi;
import com.influxdb.client.domain.InfluxQLQuery;
import com.influxdb.query.InfluxQLQueryResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * influxdb query command executor
 *
 * @author j.x
 * @date 2024/4/14 17:53
 * @since 1.1.8
 */
@Slf4j
@AutoService(QOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbQueryCommandExecutor<R> implements QOInnerCommandExecutor<R, InfluxdbQueryOperator> {

    final InfluxDBClient influxDBClient;
    final InfluxDBClientOptions clientOptions;

    public InfluxdbQueryCommandExecutor(InfluxDBClient influxDBClient, InfluxDBClientOptions clientOptions) {
        this.influxDBClient = influxDBClient;
        this.clientOptions = clientOptions;
    }

    @Override
    public List<R> doExec(InfluxdbQueryOperator operator, ListResultSetHandler<R> handler) throws Throwable {
        Table measurement = operator.getTable();
        if (measurement == null) {
            throw Exceptions.unNull("measurement is empty");
        }
        InfluxQLQueryApi influxQLQueryApi = influxDBClient.getInfluxQLQueryApi();

        // InfluxQL dsl (like SQL)
        String command = operator.getDSL();
        InfluxQLQuery influxQlQuery = new InfluxQLQuery(command, clientOptions.getBucket());
        influxQlQuery.setPrecision(InfluxQLQuery.InfluxQLPrecision.SECONDS);
        InfluxQLQueryResult result = influxQLQueryApi.query(influxQlQuery);
        ResultSet resultSet = new ResultSet();
        if (result == null) {
            return handler.apply(resultSet);
        }
        List<InfluxQLQueryResult.Result> results = result.getResults();
        // by count
        if (operator.isCount()) {
            long count = results.stream().flatMap(r -> r.getSeries().stream()).flatMap(s -> s.getValues().stream()).count();
            ResultRow resultRow = ResultRow.buildCountRow(count);
            ResultGroup resultGroup = new ResultGroup();
            resultGroup.addRow(resultRow);
            resultSet.setResultGroups(Lists.newArrayList(resultGroup));
        } else {
            List<ResultGroup> resultGroups =
                    results.stream()
                            .flatMap(r -> {
                                List<InfluxQLQueryResult.Series> series = r.getSeries();
                                return series.stream();
                            })
                            .flatMap(series -> {
                                List<InfluxQLQueryResult.Series.Record> values = series.getValues();
                                return values.stream()
                                        .map(record -> {
                                            ResultGroup resultGroup = new ResultGroup();
                                            // build row from columns
                                            Map<String, Integer> columns = series.getColumns();
                                            for (String col : columns.keySet()) {
                                                ResultRow.ResultRowBuilder rowBuilder = ResultRow.builder();
                                                rowBuilder.column(DSLName.of(col));
                                                rowBuilder.value(Optional.ofNullable(record).map(r -> r.getValueByKey(col)).orElse(null));
                                                ResultRow row = rowBuilder.build();
                                                resultGroup.addRow(row);
                                            }
                                            // build row from tags
                                            Map<String, String> tags = series.getTags();
                                            for (Map.Entry<String, String> tagEntry : tags.entrySet()) {
                                                String tagCol = tagEntry.getKey();
                                                String tagValue = tagEntry.getValue();
                                                ResultRow.ResultRowBuilder rowBuilder = ResultRow.builder();
                                                rowBuilder.column(DSLName.of(tagCol));
                                                rowBuilder.value(tagValue);
                                                ResultRow row = rowBuilder.build();
                                                resultGroup.addRow(row);
                                            }
                                            return resultGroup;
                                        });
                            })
                            .toList();
            resultSet.setResultGroups(resultGroups);
        }
        return handler.apply(resultSet);
    }
}
