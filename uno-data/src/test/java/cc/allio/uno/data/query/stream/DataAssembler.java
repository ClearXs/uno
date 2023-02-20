package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.ResourceUtil;
import cc.allio.uno.data.model.RiHis;
import com.google.common.collect.Lists;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class DataAssembler {

    private static final String URI = "http://192.168.2.29:8086";
    private static final String TOKEN = "a9g4zc4bGkahc-iiWf1FgENuFZ90nQUsu8BQ916X-wHvg2L5VJEwCQoeYJFHuL-7LLk3PA8dRhRlo8nC7-7XxQ==";
    private static final String ORG = "test";
    private static final String BUCKET = "test-bucket";

    public static List<RiHis> readForCsv(String csvFilePath) throws IOException {
        FileReader reader = new FileReader(ResourceUtil.getFile(csvFilePath));
        Iterator<CSVRecord> iterator = CSVFormat.RFC4180.withHeader(Headers.class).parse(reader).iterator();
        return Lists.newArrayList(iterator)
                .stream()
                .filter(record -> record.getRecordNumber() > 1)
                .map(record -> {
                    String z = record.get(Headers.Z);
                    String mot = record.get(Headers.MOT);
                    RiHis riHis = new RiHis();
                    try {
                        riHis.setZ(new BigDecimal(z));
                    } catch (NumberFormatException ex) {
                        riHis.setZ(BigDecimal.ZERO);
                    }
                    riHis.setMot(DateUtil.parse(mot, "dd/MM/yyyy HH:mm:ss"));
                    return riHis;
                })
                .collect(Collectors.toList());
    }

    public static List<RiHis> readForCsv() throws IOException {
        return readForCsv("classpath:query/data.csv");
    }

    /**
     * 预先把给定的measurement删除在进行插入数据
     *
     * @param measurement
     * @param data
     */
    public static void beforehandDeleteThenWriteInfluxdb(String measurement, Map<String, Collection<ValueTime>> data) {
        InfluxDBClient client = InfluxDBClientFactory.create(URI, TOKEN.toCharArray());

        DeleteApi deleteApi = client.getDeleteApi();
        Instant start = Instant.ofEpochMilli(DateUtil.parse("2022-01-01 00:00:00").getTime());
        Instant end = Instant.ofEpochMilli(DateUtil.parse("2025-12-31 00:00:00").getTime());
        deleteApi.delete(
                OffsetDateTime.ofInstant(start, ZoneId.systemDefault()),
                OffsetDateTime.ofInstant(end, ZoneId.systemDefault()),
                "_measurement=\"" + measurement + "\"",
                BUCKET,
                ORG);

        WriteApiBlocking writeApi = client.getWriteApiBlocking();

        List<Point> points = data.entrySet()
                .stream()
                .flatMap(entry ->
                        entry.getValue()
                                .stream()
                                .map(valueTime ->
                                        Point.measurement(measurement)
                                                .addField(entry.getKey(), Double.valueOf(valueTime.getValue().toString()))
                                                .time(valueTime.getTime().getTime(), WritePrecision.MS))
                )
                .collect(Collectors.toList());
        writeApi.writePoints(BUCKET, ORG, points);
    }

    enum Headers {
        Z, MOT
    }
}
