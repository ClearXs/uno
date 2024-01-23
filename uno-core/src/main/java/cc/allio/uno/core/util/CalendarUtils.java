package cc.allio.uno.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 常用时间工具
 *
 * @author jiangwei
 * @date 2022/7/6 14:18
 * @since 1.0
 */
public class CalendarUtils {
    public static final String PATTERN_DATESTART = "yyyy-MM-dd 00:00:00";

    public static final String PATTERN_DATEEND = "yyyy-MM-dd 23:59:59";

    public static final String PATTERN_UNDERLINE_DAY = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERN_UNDERLINE_FRAME = "yyyy-MM-dd HH";

    public static final String PATTERN_UNDERLINE_DAYY = "yyyy-MM-dd";

    public static final String PATTERN_UNDERLINE_MONTH = "yyyy-MM";

    public static final String PATTERN_UNDERLINE_YEAR = "yyyy";

    public static final String PATTERN_MONTH = "yyyyMM";

    public static final String PATTERN_DAY = "yyyyMMdd";

    /**
     * 获取指定 日期 的 00::00:00
     *
     * @param date
     * @return
     */
    public static Date getFirstSecondOfDay(final Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar.getTime();
    }

    /**
     * 获取指定 日期 的 23:59:59
     *
     * @param date
     * @return
     */
    public static Date getLastSecondOfDay(final Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定 日期 所在周的第一天;
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setWeekDate(cal.getWeekYear(), cal.get(Calendar.WEEK_OF_YEAR), 2);
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        return cal.getTime();
    }

    /**
     * 获取指定 日期 所在周的最后一天的时间节点
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setWeekDate(cal.getWeekYear(), cal.get(Calendar.WEEK_OF_YEAR), 2);
        cal.setWeekDate(cal.getWeekYear(), cal.get(cal.WEEK_OF_YEAR) + 1, 1);
        cal.set(cal.get(cal.YEAR),
                cal.get(cal.MONTH),
                cal.get(cal.DAY_OF_MONTH),
                23, 59, 59);
        return cal.getTime();
    }

    /**
     * 获取 00:00:00时间
     *
     * @param date
     * @return
     */
    public static Date getFirstDay(final Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定 日期 所在月的第一天的日期
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }

    /**
     * 获取指定 日期 所在月的最后一天的日期
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, last);
        return cal.getTime();
    }

    /**
     * 获取 当前时间所在 季度的第一天
     *
     * @return
     */
    public static String getFirstDayOfQuarter(final Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12) c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = shortSdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取指定 日期 所在年的第一天的日期
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfYear(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return cal.getTime();
    }

    /**
     * 获取指定 日期 所在年的最后一天的日期
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfYear(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int last = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, last);
        return cal.getTime();
    }

    /**
     * 获取n年前的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date getDateOfSomeYear(final Date date, int n) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, n);
        return cal.getTime();
    }

    /**
     * 获取n月前的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date getDateOfSomeMonth(final Date date, int n) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 计算日期 为当月的第几天
     *
     * @param date
     * @return
     */
    public static Integer getDayOfMonth(final Date date) {
        if (date == null) {
            return null;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(cal.DAY_OF_MONTH);
    }

    /**
     * 判断日期是否为 当月的第一天
     *
     * @param date
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 判断日期是否为 当季度 的第一天
     *
     * @param date
     * @return
     */
    public static boolean isFirstDayOfQuarter(Date date) {
        if (date == null) {
            return false;
        }
        SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfQuarter = getFirstDayOfQuarter(date);
        System.out.println("本季度第一天：" + firstDayOfQuarter);
        String format = shortSdf.format(date);
        if (format.equals(firstDayOfQuarter)) {
            return true;
        }
        return false;
    }

    /**
     * 判断日期是否为 当年 的第一天
     *
     * @param date
     * @return
     */
    public static boolean isFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR) == 1;
    }

    /**
     * 两个日期之间的所有时段
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenFrames(String startDate, String endDate, String pattern) {
        // 返回的日期集合
        List<String> frames = new ArrayList<>();
        Date start = DateUtil.parse(startDate, DateUtil.PATTERN_DATE);
        Date end = DateUtil.parse(endDate, DateUtil.PATTERN_DATE);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        // 日期加1(包含结束)
        tempEnd.add(Calendar.HOUR_OF_DAY, +1);
        while (tempStart.before(tempEnd)) {
            frames.add(DateUtil.format(tempStart.getTime(), pattern));
            tempStart.add(Calendar.HOUR_OF_DAY, 1);
        }
        return frames;
    }

    /**
     * 两个日期之间的天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenDays(String startDate, String endDate, String pattern) {
        // 返回的日期集合
        List<String> days = new ArrayList<>();
        Date start = DateUtil.parse(startDate, DateUtil.PATTERN_DATE);
        Date end = DateUtil.parse(endDate, DateUtil.PATTERN_DATE);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        // 日期加1(包含结束)
        tempEnd.add(Calendar.DATE, +1);
        while (tempStart.before(tempEnd)) {
            days.add(DateUtil.format(tempStart.getTime(), pattern));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    /**
     * 两个日期之间的月
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenMonths(String startDate, String endDate, String pattern) {
        // 返回的日期集合
        List<String> months = new ArrayList<>();
        Date start = DateUtil.parse(startDate, PATTERN_UNDERLINE_MONTH);
        Date end = DateUtil.parse(endDate, PATTERN_UNDERLINE_MONTH);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        // 日期加1(包含结束)
        tempEnd.add(Calendar.MONTH, +1);
        while (tempStart.before(tempEnd)) {
            months.add(DateUtil.format(tempStart.getTime(), pattern));
            tempStart.add(Calendar.MONTH, 1);
        }
        return months;
    }

    /**
     * 两个日期之间的年
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenYears(String startDate, String endDate, String pattern) {
        // 返回的日期集合
        List<String> years = new ArrayList<>();
        Date start = DateUtil.parse(startDate, PATTERN_UNDERLINE_YEAR);
        Date end = DateUtil.parse(endDate, PATTERN_UNDERLINE_YEAR);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        // 日期加1(包含结束)
        tempEnd.add(Calendar.YEAR, +1);
        while (tempStart.before(tempEnd)) {
            years.add(DateUtil.format(tempStart.getTime(), pattern));
            tempStart.add(Calendar.YEAR, 1);
        }
        return years;
    }

    /**
     * 生成指定日期时间
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return
     */
    public static Date generateDateTime(int year, int month, int day, int hour, int minute,
                                        int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return calendar.getTime();
    }

    /**
     * 获取指定 日期 的 hh:MM:00
     *
     * @param date
     * @return
     */
    public static Date formatMinuteDate(final Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 给指定日期加上指定天数
     *
     * @param
     */
    public static String addDay(String date, int day, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date newEnd = new Date();
        try {
            newEnd = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newEnd);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        Date newEndDate = calendar.getTime();
        String newDate = dateFormat.format(newEndDate);
        return newDate;
    }

    /**
     * 给指定日期加上指定月
     *
     * @param
     */
    public static String addMonth(String date, int month, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date newEnd = new Date();
        try {
            newEnd = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newEnd);
        calendar.add(Calendar.MONTH, month);
        Date newEndDate = calendar.getTime();
        String newDate = dateFormat.format(newEndDate);
        return newDate;
    }

    /**
     * 给指定日期加上指定年
     *
     * @param
     */
    public static String addYear(String date, int year, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date newEnd = new Date();
        try {
            newEnd = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newEnd);
        calendar.add(Calendar.YEAR, year);
        Date newEndDate = calendar.getTime();
        String newDate = dateFormat.format(newEndDate);
        return newDate;
    }

    /**
     * 获取某年某月有多少天
     *
     * @return 该月的天数
     */
    public static int getDaysAboutMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        //把日期设置为当月第一天
        c.set(Calendar.DATE, 1);
        //日期回滚一天，也就是最后一天
        c.roll(Calendar.DATE, -1);
        return c.get(Calendar.DATE);
    }

    /**
     * 把日期类转换为字符串
     *
     * @param
     */
    public static String switchDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        String strSwi = dateFormat.format(date);
        return strSwi;
    }

}
