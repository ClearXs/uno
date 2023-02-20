package cc.allio.uno.core.task;

import cc.allio.uno.core.cache.Cache;
import cc.allio.uno.core.cache.ConcurrentMemoryCache;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 数据堆积于缓存中，当达到指定的阈值时弹出回调计算。类似于window
 *
 * @author jiangwei
 * @date 2022/2/9 09:30
 * @see Cache
 * @see Computing
 * @since 1.0
 */
@Slf4j
public class AccumulatedTask<T> implements BatchComputingTask<T> {

    public static final int DEFAULT_THRESHOLD = 1;

    /**
     * 堆积数据最大数值
     */
    private final AtomicLong threshold;

    /**
     * 数据操作时锁对象
     */
    private final Lock lock;

    /**
     * 待计算任务回调集合
     */
    private final List<Computing<T>> numerator;

    /**
     * 缓存对象
     */
    private final Cache<T> cache;

    /**
     * 是否异步计算
     */
    private final AtomicBoolean async;

    /**
     * 创建堆积任务，使用默认的阈值来进行初始化
     *
     * @see #DEFAULT_THRESHOLD
     */
    public AccumulatedTask() {
        this(DEFAULT_THRESHOLD);
    }

    /**
     * 创建AccumulatedTask实例
     *
     * @param async 异步计算
     */
    public AccumulatedTask(boolean async) {
        this(DEFAULT_THRESHOLD, async);
    }

    /**
     * 创建堆积任务，实用默认的阈值来进行初始化
     *
     * @param cache 累积任务使用的缓存
     */
    public AccumulatedTask(Cache<T> cache) {
        this(DEFAULT_THRESHOLD, cache, false);
    }

    /**
     * 创建AccumulatedTask实例
     *
     * @param cache 指定缓存
     * @param async 是否异步计算
     */
    public AccumulatedTask(Cache<T> cache, boolean async) {
        this(DEFAULT_THRESHOLD, cache, async);
    }

    /**
     * 创建一个堆积任务，并进行初始化工作
     *
     * @param threshold 阈值
     * @throws IllegalArgumentException 当参数threshold小于0抛出
     */
    public AccumulatedTask(int threshold) {
        this(threshold, new ConcurrentMemoryCache<>(), false);
    }

    public AccumulatedTask(int threshold, boolean async) {
        this(threshold, new ConcurrentMemoryCache<>(), async);
    }

    /**
     * 创建一个堆积任务，并进行初始化工作
     *
     * @param threshold 阈值
     * @param cache     指定的缓存
     * @param async     是否进行异步计算
     * @throws IllegalArgumentException 当参数threshold小于0抛出
     */
    public AccumulatedTask(int threshold, Cache<T> cache, boolean async) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must gte 0");
        }
        this.threshold = new AtomicLong(threshold);
        this.numerator = new CopyOnWriteArrayList<>();
        this.cache = cache;
        this.lock = new ReentrantLock();
        this.async = new AtomicBoolean(async);
    }

    /**
     * 数据进行堆积，当触发阈值时将回调{@link Computing#calculate()}，并清空堆积数据
     *
     * @param data 任务数据
     * @throws NullPointerException 当输出为空时抛出
     */
    public void accumulate(@NonNull T data) {
        WindowAccumulated.windowComputing(cache, data, numerator, threshold, async, lock);
    }

    /**
     * 数据进行批量堆积，当触发阈值时将回调{@link Computing#calculate()}，并清空堆积数据
     *
     * @param data 任务数据
     * @throws NullPointerException 当输出为空时抛出
     */
    public void accumulate(List<T> data) {
        WindowAccumulated.windowComputing(cache, data, numerator, threshold, async, lock);
    }

    /**
     * 替换堆积任务阈值
     *
     * @param newThreshold 新的阈值
     * @return 替换是否成功
     * @throws IllegalArgumentException 阈值大小必须大于0
     */
    public boolean replaceThreshold(int newThreshold) {
        if (newThreshold < 0) {
            throw new IllegalArgumentException("Threshold must gte 0");
        }
        long oldThreshold = threshold.get();
        return threshold.compareAndSet(oldThreshold, newThreshold);
    }

    /**
     * @deprecated 使用@{@link #run(Computing)}或者{@link #run(List)}
     */
    @Override
    @Deprecated
    public void run() {
        log.info("Run accumulated task");
    }

    @Override
    public void run(List<Computing<T>> computingList) {
        lock.lock();
        try {
            numerator.addAll(computingList);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void finish() {
        numerator.clear();
    }

    public Cache<T> getCache() {
        return this.cache;
    }

    /**
     * 基于窗口视图的堆积任务。
     * <ol>
     *     <li>根据堆积的阈值计算出窗口大小</li>
     *     <li>取出窗口的缓存数据进行回调计算</li>
     *     <li>计算完成之后，剔除已经在窗口之外的数据</li>
     *     <li>窗口的操作在同一个事物中完成</li>
     * </ol>
     *
     * @param <T>
     */
    private static class WindowAccumulated<T> {
        private final Cache<T> cache;
        private final T computingData;
        private final List<Computing<T>> numerator;
        private final AtomicLong window;

        /**
         * 标识当前堆积数据在缓存中的索引
         */
        private final Long index;

        private WindowAccumulated(Cache<T> cache,
                                  T computingData,
                                  List<Computing<T>> numerator,
                                  AtomicLong threshold) {
            this.cache = cache;
            this.computingData = computingData;
            this.numerator = numerator;
            this.window = threshold;
            this.cache.put(computingData);
            this.index = cache.index(computingData);
        }

        /**
         * <ol>
         *     <li>计算当前堆积数据在缓存的位置</li>
         *     <li>根据阈值获取出堆积的数据，从缓存中获取区间数据[缓存大小-阈值，缓存大小]，
         *     因为计算任务是处理同一个事物中，所以就不会存在不同堆积对象产生数据错位的问题</li>
         * </ol>
         * <b>待计算数据作为堆积数据最后的索引位置传递给计算回调实例</b>
         */
        private void computing() {
            // 用于计算的指定的窗口数据
            List<T> windowData;
            // 如果当前数据不存在索引或者数据初始化时以当前数据进行计算
            if (index < 0L) {
                windowData = Collections.singletonList(computingData);
            } else {
                Long size = cache.size();
                // 当阈值大于缓存大小，默认取缓存全部数据作为计算数据
                if (window.get() > size) {
                    windowData = new ArrayList<>(cache.get());
                } else {
                    // 当窗口小于等于缓存大小，计算堆积区间的缓存数据
                    long start;
                    if (window.get() > index) {
                        // 窗口大于当前堆积数据的索引时，取区间起点为0
                        start = 0;
                    } else {
                        start = index - window.get();
                    }
                    // 包含当前数据自身
                    long end = index + 1;
                    windowData = new ArrayList<>(cache.get((int) start, (int) end));
                }
            }
            if (windowData.size() == window.get()) {
                // 回调计算数据
                computingCache(windowData);
                // 删除缓存数据
                eliminateCache();
            }
        }

        /**
         * 计算缓存的数据
         *
         * @param cacheData 缓存数据
         */
        private void computingCache(List<T> cacheData) {
            if (!CollectionUtils.isEmpty(cacheData)) {
                // 回调计算数据
                numerator.forEach(computing -> {
                    try {
                        computing.calculate(Collections.unmodifiableList(cacheData), computingData);
                    } catch (Exception e) {
                        log.error("Computing {} buffer data failed", computing.getComputingName(), e);
                    }
                });
            }
        }

        /**
         * 消除已经进行计算的数据
         * <li>
         *     <ol>取[0,index- window)区间的数据作为计算结果</ol>
         * </li>
         */
        private void eliminateCache() {
            long remove = index - window.get();
            if (remove > 0L) {
                List<T> removeCache = cache.get(0, (int) remove);
                cache.removeAll(removeCache);
            }
        }

        /**
         * 执行窗口计算
         *
         * @param cache         缓存
         * @param computingData 待计算的数据
         * @param numerator     计算者对象
         * @param threshold     阈值对象
         * @param lock          锁
         * @param <T>           范型对象
         */
        public static <T> void windowComputing(Cache<T> cache,
                                               T computingData,
                                               List<Computing<T>> numerator,
                                               AtomicLong threshold,
                                               AtomicBoolean async,
                                               Lock lock) {
            lock.lock();
            try {
                if (async.get()) {
                    CompletableFuture.runAsync(() -> new WindowAccumulated<>(cache, computingData, numerator, threshold).computing());
                } else {
                    new WindowAccumulated<>(cache, computingData, numerator, threshold).computing();
                }
            } finally {
                lock.unlock();
            }
        }

        /**
         * 执行窗口计算
         *
         * @param cache         缓存
         * @param computingData 待计算的数据集合
         * @param numerator     计算者对象
         * @param threshold     阈值对象
         * @param lock          锁
         * @param <T>           范型对象
         */
        public static <T> void windowComputing(Cache<T> cache,
                                               List<T> computingData,
                                               List<Computing<T>> numerator,
                                               AtomicLong threshold,
                                               AtomicBoolean async,
                                               Lock lock) {
            computingData.forEach(data -> windowComputing(cache, data, numerator, threshold, async, lock));
        }
    }
}
