package cc.allio.uno.core.task;

import io.netty.util.HashedWheelTimer;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 时间段任务，每隔多少时间执行一次这个任务。内部采用Netty时间轮实现
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
public class TimerSegmentTask<T> extends BaseComposedComputeTask<T> {

    /**
     * 1s执行一次时间
     */
    public static final long ONE_SECOND_ROUND = Duration.ofSeconds(1).toMillis();

    /**
     * 10s执行一次
     */
    public static final long TEN_SECOND_ROUND = Duration.ofSeconds(10).toMillis();

    /**
     * 1min执行一次
     */
    public static final long ONE_MINUTES_ROUND = Duration.ofMinutes(1).toMillis();

    /**
     * 10min执行一次
     */
    public static final long TEN_MINUTES_ROUND = Duration.ofMinutes(10).toMillis();

    /**
     * 半小时执行一次
     */
    public static final long HALF_HOURS_ROUND = Duration.ofMinutes(30).toMillis();

    /**
     * 一小时执行一次
     */
    public static final long ONE_HOURS_ROUND = Duration.ofHours(1).toMillis();

    /**
     * 六小时执行一次
     */
    public static final long SIX_HOURS_ROUND = Duration.ofHours(6).toMillis();

    /**
     * 12小时执行一次
     */
    public static final long TWELVE_HOURS_ROUND = Duration.ofHours(12).toMillis();

    /**
     * 一天执行一次
     */
    public static final long ONE_DAY_ROUND = Duration.ofDays(1).toMillis();

    /**
     * 一个月执行一次
     */
    public static final long ONE_MONTH_ROUND = ONE_DAY_ROUND * 30L;

    /**
     * 一年执行一次
     */
    public static final long ONE_YEAR_ROUND = ONE_MONTH_ROUND * 12L;

    /**
     * Netty时间轮对象
     */
    private HashedWheelTimer wheelTimer;

    /**
     * 执行周期
     */
    private long roundPeriod;

    /**
     * 标识当前时间轮是否停止，默认为
     */
    private final AtomicBoolean closed;

    /**
     * 默认时间段构造器，执行周期为{@link Integer#MAX_VALUE}
     */
    public TimerSegmentTask() {
        this(Integer.MAX_VALUE);
    }

    /**
     * @param roundPeriod 执行周期，单位ms
     */
    public TimerSegmentTask(long roundPeriod) {
        closed = new AtomicBoolean(false);
        // 时间轮刻度为1，每一格间隔roundPeriod执行，使用Netty默认的线程池
        initializeTimer(roundPeriod, TimeUnit.MILLISECONDS, 1);
    }

    /**
     * 初始化时间轮对象
     *
     * @param roundPeriod   执行周期
     * @param timeUnit      时间单位
     * @param ticksPerWheel 时间轮刻度
     */
    protected void initializeTimer(long roundPeriod, TimeUnit timeUnit, int ticksPerWheel) {
        this.roundPeriod = roundPeriod;
        this.wheelTimer = new HashedWheelTimer(roundPeriod, timeUnit, ticksPerWheel);
    }

    @Override
    public void run() {
        wheelTimer.start();
    }

    @Override
    public void run(List<Computing<T>> computingList) {
        computingList.forEach(computing ->
                wheelTimer.newTimeout(roundTask(computing, 0, TimeUnit.MILLISECONDS), 0, TimeUnit.MILLISECONDS));
        wheelTimer.start();
    }

    @Override
    public void finish() {
        wheelTimer.stop();
        closed.compareAndSet(false, true);
    }

    @Override
    public void addComputableTask(Computing<T> task) {
        wheelTimer.newTimeout(roundTask(task, 0, TimeUnit.MILLISECONDS), 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 循环重复的任务
     *
     * @param computing 计算任务实例
     * @param delay     任务延迟多少后开始
     * @param timeUnit  时间单位
     * @return TimerTask实例
     */
    protected TimerTask roundTask(Computing<T> computing, long delay, TimeUnit timeUnit) {
        return timeout -> {
            try {
                // 当没有异步关闭时间轮就进行计算
                if (!closed.get()) {
                    computing.calculate();
                    wheelTimer.newTimeout(roundTask(computing, delay, timeUnit), delay, timeUnit);
                }
            } catch (Exception e) {
                log.error("Timer rule task failed", e);
            }
        };
    }

    /**
     * 获取当前时间段任务执行周期
     *
     * @return 时间段数值
     */
    public long getRoundPeriod() {
        return this.roundPeriod;
    }

    /**
     * 返回默认以1000ms为基准的执行周期
     *
     * @param round 执行周期
     * @return MILL的long数据
     */
    public static long toRoundMill(long round) {
        return toRoundMill(round, 1000L);
    }

    /**
     * 返回以基准时间的执行周期，如1，基准为1s，那么返回1s的long数据
     *
     * @param round  执行周期
     * @param basics 执行基准
     * @return MILL的long数据
     */
    public static long toRoundMill(long round, long basics) {
        return round * basics;
    }

}
