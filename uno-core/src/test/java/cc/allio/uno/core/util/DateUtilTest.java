package cc.allio.uno.core.util;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;

/**
 * 时间工具类测试
 *
 * @author jiangwei
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

    @Override
    protected void onInit() throws Throwable {

    }

    @Override
    protected void onDown() throws Throwable {

    }
}
