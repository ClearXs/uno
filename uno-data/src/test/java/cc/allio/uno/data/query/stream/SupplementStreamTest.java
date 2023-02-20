package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.mybatis.support.TestQueryFilter;
import cc.allio.uno.data.mybatis.support.param.GenericQuery;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.model.RiHis;
import cc.allio.uno.data.model.RiReal;
import cc.allio.uno.data.query.param.QuerySetting;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

class SupplementStreamTest extends BaseTestCase {

    TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();

    void initData() {
        RiReal riReal2 = new RiReal();
        riReal2.setZ(BigDecimal.valueOf(2L));
        riReal2.setMot(DateUtil.parse("2022-07-02 15:35:00"));
        RiReal riReal1 = new RiReal();
        riReal1.setZ(BigDecimal.ONE);
        riReal1.setMot(DateUtil.parse("2022-07-02 15:10:00"));
        RiReal riReal3 = new RiReal();
        riReal3.setZ(BigDecimal.valueOf(4L));
        riReal3.setMot(DateUtil.parse("2022-07-02 17:35:00"));
        dataStream.add(riReal1);
        dataStream.add(riReal2);
        dataStream.add(riReal3);
    }

    @Test
    void testIncrement() throws Throwable {
        initData();
        CollectionTimeStream<RiReal> supplementStream = new StreamBuilder<>(dataStream).sort().supplement().build();
        QueryFilter queryFilter = new QueryFilter();
        GenericQuery query = TestQueryFilter.mockGenericQuery("1H", "DEFAULT");
        query.setSetting(new QuerySetting().setSupplement(true));
        queryFilter.addQueryWrapper(query);
        Collection<RiReal> result = new ReadStream<>(supplementStream).read(queryFilter);
        // 以数据时间最小间隔20min
        assertEquals(4, result.size());

        // 新增一条数据，按照计算他应该
        RiReal riReal = new RiReal();
        riReal.setZ(BigDecimal.valueOf(2L));
        // 以数据的最小时间间隔20min，导致会新增58条 再加上已经存在的2条，则最终数量为61
        riReal.setMot(DateUtil.parse("2022-07-03 15:10:00"));
        dataStream.add(riReal);
        result = new ReadStream<>(supplementStream).read(queryFilter);
        assertEquals(26, result.size());
    }

    /**
     * Test Case: 无数据或者一条数据据时取时间间隔
     */
    @Test
    void testZeroAndOneTimeSpacing() throws Throwable {
        initData();
        TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setSupplement(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        SupplementTimeStream<RiReal> supplementStream = new SupplementTimeStream<>(new SortStream<>(dataStream));
        long timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
        assertEquals(Duration.ofHours(1L).toMillis(), timeSpacing);

        RiReal riReal1 = new RiReal();
        riReal1.setMot(DateUtil.parse("2022-07-03 15:10:00"));
        dataStream.add(riReal1);
        timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
        assertEquals(Duration.ofHours(1L).toMillis(), timeSpacing);
        RiReal riRea2 = new RiReal();
        riRea2.setMot(DateUtil.parse("2022-07-03 15:30:00"));
        dataStream.add(riRea2);
        timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
        assertEquals(Duration.ofMinutes(20L).toMillis(), timeSpacing);
    }

    @Test
    void testLimitedTimeSpacing() throws Throwable {
        RiReal riReal1 = new RiReal();
        riReal1.setMot(DateUtil.parse("2022-07-03 15:10:00"));
        RiReal riReal2 = new RiReal();
        riReal2.setMot(DateUtil.parse("2022-07-03 15:30:00"));
        RiReal riReal3 = new RiReal();
        riReal3.setMot(DateUtil.parse("2022-07-03 17:30:00"));
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setSupplement(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        // 3条数据
        TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();
        dataStream.add(riReal1);
        dataStream.add(riReal2);
        dataStream.add(riReal3);
        SupplementTimeStream<RiReal> supplementStream = new SupplementTimeStream<>(new SortStream<>(dataStream));
        long timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
        assertEquals(Duration.ofMinutes(20L).toMillis(), timeSpacing);

        // 数据之间超过给定的1H
        dataStream.clear();
        dataStream.add(riReal1);
        dataStream.add(riReal3);
        timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
        assertEquals(Duration.ofHours(1L).toMillis(), timeSpacing);

        // 分隔成2个区间，求两个区间最小值时间
        dataStream.clear();
        RiReal riReal4 = new RiReal();
        riReal4.setMot(DateUtil.parse("2022-07-03 16:30:00"));
        dataStream.add(riReal1);
        dataStream.add(riReal2);
        dataStream.add(riReal3);
        ;
        dataStream.add(riReal4);
        // 值可能1200000 OR 3600000
        timeSpacing = supplementStream.timeSpacing(query, dataStream.read(query.build()).collectList().block());
    }

    @Test
    void testUnlimited() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setSupplement(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        RiReal riReal = new RiReal();
        riReal.setZ(BigDecimal.ZERO);
        riReal.setMot(DateUtil.parse("2022-08-01 00:00:00"));
        dataStream.add(riReal);
        ValueTimeStream stream = new ValueTimeStream(new SupplementTimeStream<>(new SortStream<>(dataStream)));
        Map<String, Collection<ValueTime>> result = stream.read(query.build());
        for (Map.Entry<String, Collection<ValueTime>> entry : result.entrySet()) {
            System.out.println(entry);
        }
    }

    @Test
    void testBigData() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setSupplement(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        List<RiHis> read = DataAssembler.readForCsv();
        dataStream.addAll(read);
        CollectionTimeStream<RiReal> supplementStream = new StreamBuilder<>(dataStream).sort().supplement().build();
        Duration verify = supplementStream.read(query.build())
                .as(StepVerifier::create)
                .expectTimeout(Duration.ofSeconds(2L))
                .verify();
        System.out.println(verify);
    }

    @Test
    void testData20221212() throws Throwable {
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setSupplement(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        List<RiHis> read = DataAssembler.readForCsv("classpath:query/data-2022-12-12.csv");
        dataStream.addAll(read);
        CollectionTimeStream<RiReal> supplementStream = new StreamBuilder<>(dataStream).sort().supplement().build();
        Collection<RiReal> result = new ReadStream<>(supplementStream).read(query.build());
        System.out.println(result);
    }
}
