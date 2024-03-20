package cc.allio.uno.core.task;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <b>Cron表达式是一个字符串，字符串为5个空格隔开，分为6个域，每个域代表一个含义</b>
 * <p/>
 * <b>CronTask</b>使用Cron表达式来指定任务在什么时段定时执行。
 *
 * @author j.x
 * @date 2021/12/30 15:41
 * @see CronExpression
 * @since 1.0
 */
public class CronTask<T> extends TimerSegmentTask<T> {

    /**
     * 每秒执行一次
     */
    public static final String EXECUTE_PER_SECOND = "0/1 * * * * ?";

    /**
     * 每半分钟执行一次
     */
    public static final String EXECUTE_HALF_MINUTE = "0/30 * * * * ?";

    /**
     * 每分钟执行一次
     */
    public static final String EXECUTE_PER_MINUTE = "0 0/1 * * * ?";

    /**
     * 每小时执行一次
     */
    public static final String EXECUTE_PER_HOUR = "0 0 0/1 * * ?";

    /**
     * 每天凌晨12点执行一次
     */
    public static final String EXECUTE_PER_DAY = "0 0 0 1/1 * ?";

    /**
     * 每月最后一天执行凌晨12点执行
     */
    public static final String EXECUTE_PER_MONTH = "0 0 0 L * ?";

    /**
     * {@link CronTask}默认构造方法。默认每秒执行一次
     */
    public CronTask() throws ParseException {
        this(EXECUTE_PER_SECOND);
    }

    /**
     * {@link CronTask}构造方法，传递Cron表达式。
     *
     * @param cronExpression Cron表达式
     * @see CronExpression
     */
    public CronTask(String cronExpression) throws ParseException {
        CronExpression expression = new CronExpression(cronExpression);
        Date now = new Date();
        long roundPeriod = expression.getNextValidTimeAfter(now).getTime() - now.getTime();
        initializeTimer(roundPeriod, TimeUnit.MILLISECONDS, 1);
    }
}
