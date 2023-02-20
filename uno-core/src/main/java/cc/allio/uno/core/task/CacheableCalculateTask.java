package cc.allio.uno.core.task;

import cc.allio.uno.core.cache.CacheKey;
import cc.allio.uno.core.util.CoreBeanUtil;
import cc.allio.uno.core.cache.Cache;
import cc.allio.uno.core.cache.InitializationCache;
import cc.allio.uno.core.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.text.ParseException;
import java.util.List;
import java.util.function.Consumer;

/**
 * 可以缓存计算的任务。<br/>
 * <i>1.创建下游信号源Sink，完成计算逻辑，向下游传递数据。</i></br>
 * <i>
 * 2.缓存采取Redis缓存。目的是任务是定时触发，当服务重启后，内存数据被清空，数据丢失。数据采用redis进行缓存。
 * 在初始化时候会从redis中读取数据，由信号源触发下游处理链缓存数据未处理完成数据。在定时完成后会把数据清空。</i>
 *
 * @author jiangwei
 * @date 2021/12/24 16:44
 * @since 1.0
 */
@Slf4j
public class CacheableCalculateTask<T> extends ProxyBufferTimerSegmentTask<T> {

    /**
     * 缓存统计数据数据触发源
     */
    private FluxSink<T> bufferSink;

    private final Cache<T> cache;

    /**
     * 创建基于Redis缓存定时的计算的任务
     *
     * @param cronExpression Cron表达式
     * @param cacheKey       Key
     * @throws ParseException Cron表达式解析错误时抛出
     */
    public CacheableCalculateTask(String cronExpression,
                                  CacheKey cacheKey,
                                  Class<? extends T> actualType) throws ParseException {
        this(cronExpression, EmitterProcessor.create(false), cacheKey, actualType);
    }

    /**
     * 创建基于Redis缓存定时的计算的任务
     *
     * @param cronExpression Cron表达式
     * @param dataPublisher  数据发布者
     * @param cacheKey       Key
     * @throws ParseException                Cron表达式解析错误时抛出
     * @throws NullPointerException          {@link CoreBeanUtil}的{@link ApplicationContext}为空是抛出
     * @throws NoSuchBeanDefinitionException {@link CoreBeanUtil#getBean(Class)}没有找到bean时抛出
     */
    public CacheableCalculateTask(String cronExpression,
                                  EmitterProcessor<T> dataPublisher,
                                  CacheKey cacheKey,
                                  Class<? extends T> actualType) throws ParseException {
        super(cronExpression);
        StringRedisTemplate redisTemplate = CoreBeanUtil.getBean(StringRedisTemplate.class);
        this.cache = new InitializationCache<>(
                new RedisCache<>(cacheKey, redisTemplate, actualType),
                (cache, buffer) ->
                        dataPublisher
                                .transform(flux -> {
                                    bufferSink = ((EmitterProcessor<T>) flux).sink(FluxSink.OverflowStrategy.BUFFER);
                                    return Flux.fromIterable(buffer);
                                })
                                .onErrorContinue((error, sequential) -> log.error("Deal cacheable data failed", error))
                                .subscribe(sequential -> bufferSink.next(sequential)));
        dataPublisher.subscribe(cache::put);
    }

    /**
     * 需要保存到缓存中触发
     *
     * @param sequential 时序数据对象
     */
    public void onSaveBuffer(T sequential) {
        bufferSink.next(sequential);
    }

    /**
     * 定时器到时触发，每调用一次都会定时器添加一个任务。
     *
     * @param calculator 真正执行计算的对象
     */
    public void onCalculate(Consumer<List<T>> calculator) {
        addComputableTask((buffer, current) -> calculator.accept(buffer));
    }

    @Override
    protected Cache<T> getCache() {
        return cache;
    }
}
