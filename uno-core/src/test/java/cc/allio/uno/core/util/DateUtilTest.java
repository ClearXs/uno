package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;

/**
 * 时间工具类测试
 *
 * @author j.x
 * @date 2022/7/6 11:56
 * @since 1.0
 */
class DateUtilTest extends BaseTestCase {

    @Test
    void testGetHourDataSetFormat() {
        Collection<String> hourDataSetFormat = DateUtil.getHourDataSetFormat();
        assertEquals(24, hourDataSetFormat.size());
        for (String data : hourDataSetFormat) {
            System.out.println(data);
        }
    }

    @Test
    void testGetDayDataSetFormat() {
        Collection<String> dayDataSetFormat = DateUtil.getDayDataSetFormat();
        for (String date : dayDataSetFormat) {
            System.out.println(date);
        }
    }

    @Test
    void testGetMonthDataSetFormat() {
        Collection<String> monthDataSetFormat = DateUtil.getMonthDataSetFormat();
        for (String month : monthDataSetFormat) {
            System.out.println(month);
        }
    }

    @Test
    void testIsBetween() {
        // [-1. 1]
        Date now = DateUtil.now();
        Date endTime = DateUtil.plusHours(now, 1);
        Date startTime = DateUtil.minusHours(now, 1);
        assertTrue(DateUtil.isBetween(now, startTime, endTime));

        // [-1, 1]

        now = DateUtil.minusHours(now, 2);
        assertFalse(DateUtil.isBetween(now, startTime, endTime));

    }

    @Test
    void testIsoParse() {
        String isoStr = "2023-11-30T08:48:19.304+08:00";
        Date parse = DateUtil.parse(isoStr);
        assertNotNull(parse);
    }

    @Test
    void testUniParse() {
        String str1 = "2023-11-30 08:48:19";
        Date d1 = DateUtil.parse(str1);
        assertNotNull(d1);
        String str2 = "2x23-11-30 08:48:19d";
        Date d2 = DateUtil.parse(str2);
        assertNull(d2);
        String str3 = "2023-11-30";
        Date d3 = DateUtil.parse(str3);
        assertNotNull(d3);
    }
}
