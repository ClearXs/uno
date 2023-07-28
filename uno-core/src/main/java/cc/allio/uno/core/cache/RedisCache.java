package cc.allio.uno.core.cache;

import cc.allio.uno.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * 基于Redis缓存<br/>
 * <b>使用CRUD会造成大量的IO开销，导致系统吞吐量下降。</b>
 *
 * @author jiangwei
 * @date 2022/2/9 14:30
 * @since 1.0
 */
@Slf4j
public class RedisCache<T> implements Cache<T> {

    /**
     * redis缓存key
     */
    private final CacheKey cacheKey;

    /**
     * redis缓存实例
     */
    private final StringRedisTemplate redisTemplate;

    /**
     * Redis缓存的具体类型
     */
    private final Class<? extends T> actualType;

    /**
     * 比较器
     */
    private final BiPredicate<T, T> comparator;

    private final Object lock = new Object();

    public RedisCache(CacheKey cacheKey,
                      StringRedisTemplate redisTemplate,
                      Class<? extends T> actualType) {
        this(cacheKey, redisTemplate, actualType, null);
    }

    public RedisCache(CacheKey cacheKey,
                      StringRedisTemplate redisTemplate,
                      Class<? extends T> actualType,
                      BiPredicate<T, T> comparator) {
        this.cacheKey = cacheKey;
        this.redisTemplate = redisTemplate;
        this.actualType = actualType;
        this.comparator = comparator;
    }



    /**
     * 如果存在则不放入缓存中
     */
    @Override
    public T put(T cache) {
        try {
            synchronized (lock) {
                Long index = index(cache);
                if (index < 0) {
                    redisTemplate.opsForList().rightPush(cacheKey.getKey(), JsonUtils.toJson(cache));
                }
            }
        } catch (Exception e) {
            log.error("Put into Redis cache failed", e);
            return null;
        }
        return cache;
    }

    /**
     * 如果存在则不放入缓存中
     *
     * @param caches 数据集合
     */
    @Override
    public void putAll(List<T> caches) {
        synchronized (lock) {
            // 获取不存在于缓存中的缓存数据
            List<T> pushCache = caches.stream()
                    .filter(cache -> index(cache) < 0)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pushCache)) {
                redisTemplate.opsForList().rightPushAll(
                        cacheKey.getKey(),
                        pushCache
                                .stream()
                                .map(JsonUtils::toJson)
                                .collect(Collectors.toList()));
            }

        }
    }

    @Override
    public T get(int index) {
        List<T> cacheAll = get();
        return cacheAll.get(index);
    }

    @Override
    public List<T> get(int start, int end) {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start = " + start);
        }
        if (end > size()) {
            throw new IndexOutOfBoundsException("end = " + end);
        }
        if (end < start) {
            throw new IllegalArgumentException("start(" + start +
                    ") > end(" + end + ")");
        }
        List<String> buffer = redisTemplate.opsForList().range(cacheKey.getKey(), start, end);
        if (CollectionUtils.isEmpty(buffer)) {
            throw new IndexOutOfBoundsException("start(" + start +
                    ") end(" + end + ")");
        }
        return buffer
                .stream()
                .map(cache -> JsonUtils.parse(cache, actualType))
                .collect(Collectors.toList());
    }

    @Override
    public List<T> get() {
        List<String> buffer = redisTemplate.opsForList().range(cacheKey.getKey(), 0, -1);
        if (CollectionUtils.isEmpty(buffer)) {
            return Collections.emptyList();
        }
        return buffer
                .stream()
                .map(cache -> JsonUtils.parse(cache, actualType))
                .collect(Collectors.toList());
    }

    @Override
    public void remove(int index) {
        List<T> cacheAll = get();
        synchronized (lock) {
            cacheAll.remove(index);
            clear();
            putAll(cacheAll);
        }
    }

    @Override
    public void remove(T o) {
        List<T> cacheAll = get();
        synchronized (lock) {
            Long index = index(o);
            cacheAll.remove(index.intValue());
            clear();
            putAll(cacheAll);
        }
    }

    @Override
    public void clear() {
        redisTemplate.delete(cacheKey.getKey());
    }

    @Override
    public BiPredicate<T, T> comparator() {
        return comparator;
    }

    @Override
    public Long size() {
        return redisTemplate.opsForList().size(cacheKey.getKey());
    }

    /**
     * 给Key设置过期时间
     */
    public void setExpirationTime(long timeOut,TimeUnit timeUnit){
        try {
            synchronized (lock) {
                List<T> cacheList = get();
                if ((cacheList.size()) > 0) {
                    redisTemplate.expire(cacheKey.getKey(),timeOut,timeUnit);
                }else {
                    log.info(String.format("There is no %s key in redis",cacheKey.getKey()));
                }
            }
        } catch (Exception e) {
            log.error("Set Expiration Time failed", e);
        }

    }
}
