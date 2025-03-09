package cc.allio.uno.data.orm.executor.influxdb.internal;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.orm.dsl.DSLName;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.influxdb.dml.InfluxdbInsertOperator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultRow;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.internal.IOInnerCommandExecutor;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * influxdb insert command executor
 *
 * @author j.x
 * @see InfluxdbInsertOperator
 * @since 1.1.8
 */
@Slf4j
@AutoService(IOInnerCommandExecutor.class)
@CommandExecutor.Group(ExecutorKey.INFLUXDB_LITERAL)
public class InfluxdbInsertCommandExecutor implements IOInnerCommandExecutor<InfluxdbInsertOperator> {

    final InfluxDBClient influxDBClient;

    public InfluxdbInsertCommandExecutor(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    @Override
    public Boolean doExec(InfluxdbInsertOperator operator, ResultSetHandler<Boolean> handler) throws Throwable {
        Table measurement = operator.getTable();
        if (measurement == null) {
            throw Exceptions.unNull("measurement is empty");
        }
        WriteApiBlocking writeApiBlocking = influxDBClient.getWriteApiBlocking();
        // build to write point
        List<Map<DSLName, Object>> fields = operator.getFields();
        List<Map<DSLName, Object>> tags = operator.getTags();
        int size = Math.max(fields.size(), tags.size());
        List<Point> points = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Point point = new Point(measurement.getName().format());
            // set to field, maybe field size less than var i
            if (fields.size() - 1 >= i) {
                Map<DSLName, Object> field = fields.get(i);
                for (Map.Entry<DSLName, Object> fieldEntry : field.entrySet()) {
                    DSLName dslName = fieldEntry.getKey();
                    String fieldName = dslName.format();
                    Object value = fieldEntry.getValue();
                    // set to specific type
                    switch (value) {
                        case Boolean b -> point.addField(fieldName, b);
                        case Integer n -> point.addField(fieldName, n);
                        case Long l -> point.addField(fieldName, l);
                        case Double d -> point.addField(fieldName, d);
                        case Short s -> point.addField(fieldName, s);
                        case Float f -> point.addField(fieldName, f.doubleValue());
                        case BigDecimal f -> point.addField(fieldName, f.doubleValue());
                        case null, default -> {
                            // set to string
                            String stringValue = Types.toString(value);
                            point.addField(fieldName, stringValue);
                        }
                    }
                }
            }

            // set to tags, maybe tag size less than var i
            if (tags.size() - 1 >= i) {
                Map<DSLName, Object> tag = tags.get(i);
                for (Map.Entry<DSLName, Object> tagEntry : tag.entrySet()) {
                    DSLName dslName = tagEntry.getKey();
                    String tagName = dslName.format();
                    Object value = tagEntry.getValue();
                    String stringValue = Types.toString(value);
                    point.addTag(tagName, stringValue);
                }
            }
            // set current time as influx timestamp
            point.time(DateUtil.now().getTime(), WritePrecision.MS);
            points.add(point);
        }
        ResultRow resultRow;
        try {
            writeApiBlocking.writePoints(points);
            resultRow = ResultRow.buildUpdateRow(true);
        } catch (Throwable ex) {
            log.error("Failed insert measurement [{}]", measurement, ex);
            resultRow = ResultRow.buildUpdateRow(false);
        }
        ResultGroup resultGroup = new ResultGroup();
        resultGroup.addRow(resultRow);
        return handler.apply(resultGroup);
    }
}
