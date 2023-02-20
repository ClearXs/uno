package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.data.mybatis.support.param.GenericQuery;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.param.QuerySetting;
import cc.allio.uno.data.model.RiReal;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

class OutliersIgnoreStreamTest extends BaseTestCase {

    TestDataTimeStream<RiReal> dataStream = new TestDataTimeStream<>();

    @Test
    void testThrowException() {
        CollectionTimeStream<RiReal> outliersIgnoreStream = new StreamBuilder<>(dataStream).outliersIgnore().build();
        assertThrows(IllegalArgumentException.class, () -> outliersIgnoreStream.read(new QueryFilter()));
    }

    @Test
    void testEmptyOrNull() throws Throwable {
        CollectionTimeStream<RiReal> outliersIgnoreStream = new StreamBuilder<>(dataStream).outliersIgnore().build();
        GenericQuery query = new GenericQuery();
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        Collection<RiReal> result = new ReadStream(outliersIgnoreStream).read(query.build());
        assertEquals(0, result.size());
    }

    /**
     * Test Case: 过滤
     */
    @Test
    void testFilterNull() throws Throwable {
        CollectionTimeStream<RiReal> outliersIgnoreStream = new StreamBuilder<>(dataStream).outliersIgnore().build();
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setFilterOutliers(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        RiReal riReal1 = new RiReal();
        riReal1.setZ(BigDecimal.ZERO);
        RiReal riReal2 = new RiReal();
        riReal2.setZ(null);
        dataStream.add(riReal1);
        dataStream.add(riReal2);
        Collection<RiReal> result = new ReadStream(outliersIgnoreStream).read(query.build());
        assertEquals(0, result.size());
    }

    @Test
    void testRepeatData() throws Throwable {
        CollectionTimeStream<RiReal> outliersIgnoreStream = new StreamBuilder<>(dataStream).outliersIgnore().build();
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setFilterOutliers(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        Date now = DateUtil.now();
        RiReal riReal1 = new RiReal();
        riReal1.setZ(BigDecimal.ZERO);
        riReal1.setMot(now);
        RiReal riReal2 = new RiReal();
        riReal2.setMot(now);
        riReal2.setZ(BigDecimal.ZERO);
        dataStream.add(riReal1);
        dataStream.add(riReal2);
        Collection<RiReal> result = new ReadStream(outliersIgnoreStream).read(query.build());
        assertEquals(1, result.size());
    }

    @Test
    void testUnlimited() throws Throwable {
        CollectionTimeStream<RiReal> outliersIgnoreStream = new StreamBuilder<>(dataStream).outliersIgnore().build();
        GenericQuery query = new GenericQuery();
        query.setSetting(new QuerySetting().setFilterOutliers(true));
        query.setTimeColumn("mot");
        query.setDataColumns(new String[]{"z"});
        Date now = DateUtil.now();
        for (int i = 0; i < 100000; i++) {
            RiReal riReal = new RiReal();
            if (i % 2 == 0) {
                riReal.setZ(BigDecimal.ONE);
                riReal.setMot(now);
            }
            dataStream.add(riReal);
        }
        Collection<RiReal> result = new ReadStream(outliersIgnoreStream).read(query.build());
        assertEquals(1, result.size());
    }

}
