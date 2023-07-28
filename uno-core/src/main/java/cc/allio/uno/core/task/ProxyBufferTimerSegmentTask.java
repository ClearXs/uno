package cc.allio.uno.core.task;

import cc.allio.uno.core.cache.Cache;
import cc.allio.uno.core.cache.ConcurrentMemoryCache;
import cc.allio.uno.core.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.text.ParseException;

/**
 * 采用Flux作为数据源发布,可缓存的Cron定时任务，计算任务采用代理进行实现。
 *
 * @author jiangwei
 * @date 2021/12/22 20:45
 * @since 1.0
 */
@Slf4j
public class ProxyBufferTimerSegmentTask<T> extends CronTask<T> {

    private volatile Cache<T> cache;

    /**
     * {@link ProxyBufferTimerSegmentTask}构造器
     *
     * @param cronExpression Cron表达式
     */
    public ProxyBufferTimerSegmentTask(String cronExpression) throws ParseException {
        super(cronExpression);
    }

    /**
     * {@link ProxyBufferTimerSegmentTask}构造器
     *
     * @param cronExpression Cron表达式
     */
    public ProxyBufferTimerSegmentTask(String cronExpression, Flux<T> dataPublisher) throws ParseException {
        super(cronExpression);
        dataPublisher.subscribe(getCache()::put);
    }

    @Override
    public void addComputableTask(Computing<T> task) {
        Computing<T> proxyTask = ProxyFactory.<T>proxy().newProxyInstance(Computing.class, (proxy, method, args) -> {
            // 父类调用只会触发calculate()方法
            try {
                task.calculate(getCache().get());
                getCache().clear();
            } catch (Exception e) {
                log.error("Compute task failed", e);
            }
            return null;
        });
        super.addComputableTask(proxyTask);
    }

    protected Cache<T> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    cache = new ConcurrentMemoryCache<>();
                }
            }
        }
        return cache;
    }

}
