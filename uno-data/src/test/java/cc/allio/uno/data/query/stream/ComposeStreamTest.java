package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.mybatis.support.param.GenericQuery;
import cc.allio.uno.data.model.RiHis;
import cc.allio.uno.data.model.RiReal;
import cc.allio.uno.data.query.param.QuerySetting;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

class ComposeStreamTest extends BaseTestCase {

    TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();

    @Test
    void testSupplementAndOutliersIgnoreForData20221212() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        query.setSetting(new QuerySetting().setFilterOutliers(true));
        List<RiHis> read = DataAssembler.readForCsv("classpath:query/data-2022-12-12.csv");
        dataStream.addAll(read);

        ValueTimeStream stream = new ValueTimeStream(new StreamBuilder<>(dataStream).sort().supplement().outliersIgnore().build());
        Map<String, Collection<ValueTime>> result = stream.read(query.build());
        assertEquals(54, result.get("z").size());

        DataAssembler.beforehandDeleteThenWriteInfluxdb("data-2022-12-12", result);
    }

    @Test
    void testSupplementAndOutliersIgnoreForData20221208() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        query.setSetting(new QuerySetting().setFilterOutliers(true).setSupplement(true));
        List<RiHis> read = DataAssembler.readForCsv("classpath:query/data-2022-12-08.csv");
        dataStream.addAll(read);

        ValueTimeStream stream = new ValueTimeStream(new StreamBuilder<>(dataStream).sort().supplement().outliersIgnore().build());
        Map<String, Collection<ValueTime>> result = stream.read(query.build());
        assertEquals(288, result.get("z").size());

        DataAssembler.beforehandDeleteThenWriteInfluxdb("data-2022-12-08", result);
    }

    @Test
    void testMoreBigData() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        query.setSetting(new QuerySetting().setFilterOutliers(true).setSupplement(true));
        List<RiHis> read = DataAssembler.readForCsv("classpath:query/big-data.csv");
        dataStream.addAll(read);
        ValueTimeStream stream = new ValueTimeStream(new StreamBuilder<>(dataStream).sort().supplement().outliersIgnore().build());
        Map<String, Collection<ValueTime>> result = stream.read(query.build());


    }
}
