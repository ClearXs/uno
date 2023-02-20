package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.mybatis.support.TestQueryFilter;
import cc.allio.uno.data.mybatis.support.param.GenericQuery;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.model.RiReal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;

class DiluentStreamTest extends Assertions {

    TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();

    @BeforeEach
    void init() throws Throwable {
        RiReal riReal1 = new RiReal();
        riReal1.setZ(BigDecimal.ONE);
        riReal1.setMot(DateUtil.parse("2022-07-02 15:10:00"));
        dataStream.add(riReal1);
        RiReal riReal2 = new RiReal();
        riReal2.setZ(BigDecimal.valueOf(2L));
        riReal2.setMot(DateUtil.parse("2022-07-02 15:35:00"));
        dataStream.add(riReal2);
        RiReal riReal3 = new RiReal();
        riReal3.setZ(BigDecimal.valueOf(4L));
        riReal3.setMot(DateUtil.parse("2022-07-02 17:35:00"));
        dataStream.add(riReal3);
    }

    @Test
    void testEmptyOrNull() throws Throwable {
        // empty
        dataStream.clear();
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("1H", "DEFAULT");
        Collection<RiReal> read = new ReadStream<>(diluentStream).read(genericQuery.build());
        assertEquals(0, read.size());
    }

    @Test
    void testIgnoreDilute() throws Throwable {
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("1H", "DEFAULT");
        Collection<RiReal> read = new ReadStream<>(diluentStream).read(genericQuery.build());
        assertEquals(2, read.size());
    }

    /**
     * Test Case: 1H数据抽稀
     */
    @Test
    void test1HDilute() throws Throwable {
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("1H", "DEFAULT");
        QueryFilter queryFilter = genericQuery.build();
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        Collection<RiReal> result = new ReadStream<>(diluentStream).read(queryFilter);
        assertEquals(2, result.size());
    }

    /**
     * Test Case: 6H数据抽稀
     */
    @Test
    void test6HDilute() throws Throwable {
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("6H", "DEFAULT");
        QueryFilter queryFilter = genericQuery.build();
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        Collection<RiReal> result = new ReadStream<>(diluentStream).read(queryFilter);
        assertEquals(1, result.size());
    }

    /**
     * Test Case: 6H抽稀后进行ADD
     */
    @Test
    void test6HThenAndAction() throws Throwable {
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("6H", "ADD");
        QueryFilter queryFilter = genericQuery.build();
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        Collection<RiReal> result = new ReadStream<>(diluentStream).read(queryFilter);
        assertEquals(1, result.size());
        RiReal real = get(0, result);
        assertEquals(BigDecimal.valueOf(7).setScale(2), real.getZ());
    }

    /**
     * Test Case: 测试相同时间点上数据
     */
    @Test
    void testSameTimePoint() throws Throwable {
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("1H", "ADD");
        dataStream.clear();

        QueryFilter queryFilter = genericQuery.build();

        RiReal riReal1 = new RiReal();
        riReal1.setZ(BigDecimal.ONE);
        riReal1.setMot(DateUtil.parse("2022-07-02 15:10:00"));

        dataStream.add(riReal1);
        RiReal riReal2 = new RiReal();
        riReal2.setZ(BigDecimal.ONE);
        riReal2.setMot(DateUtil.parse("2022-07-02 16:10:00"));
        RiReal riReal3 = new RiReal();
        riReal3.setZ(BigDecimal.ONE);
        riReal3.setMot(DateUtil.parse("2022-07-02 16:10:00"));
        RiReal riReal4 = new RiReal();
        riReal4.setZ(BigDecimal.ONE);
        riReal4.setMot(DateUtil.parse("2022-07-02 16:10:00"));
        dataStream.add(riReal2);
        dataStream.add(riReal3);
        dataStream.add(riReal4);
        RiReal riReal5 = new RiReal();
        riReal5.setZ(BigDecimal.ONE);
        riReal5.setMot(DateUtil.parse("2022-07-02 17:10:00"));
        dataStream.add(riReal5);

        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(dataStream);
        Collection<RiReal> result = new ReadStream<>(diluentStream).read(queryFilter);
        assertEquals(3, result.size());

    }

    /**
     * Test Case: 测试包含{@link SupplementTimeStream}流的数据
     */
    @Test
    void testSuppleStreamAddAction() throws Throwable {
        GenericQuery genericQuery = TestQueryFilter.mockGenericQuery("1H", "ADD");
        QueryFilter queryFilter = genericQuery.build();
        DiluentTimeStream<RiReal> diluentStream = new DiluentTimeStream<>(new SupplementTimeStream<>(dataStream));
        Collection<RiReal> result = new ReadStream<>(diluentStream).read(queryFilter);
        assertEquals(4, result.size());
    }

    public <T> T get(int index, Collection<T> c) {
        return (T) c.toArray()[index];
    }
}
