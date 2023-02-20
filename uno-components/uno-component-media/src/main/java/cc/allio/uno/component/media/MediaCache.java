package cc.allio.uno.component.media;

import cc.allio.uno.component.media.entity.Media;
import cc.allio.uno.core.cache.Cache;
import cc.allio.uno.core.cache.CacheKey;
import cc.allio.uno.core.cache.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * 基于堆内存作为缓存
 *
 * @author jiangwei
 * @date 2022/4/4 16:23
 * @since 1.0.6
 */
public class MediaCache implements Cache<Media> {

    private final Cache<Media> heapCache;

    public MediaCache(StringRedisTemplate redisTemplate) {
        heapCache = new RedisCache<>(
                CacheKey.of(CacheKey.COMPANY_PREFIX + ":medias"),
                redisTemplate,
                Media.class,
                (s, o) -> s.getId().equals(o.getId()));
    }

    @Override
    public Media put(Media cache) {
        return heapCache.put(cache);
    }

    @Override
    public void putAll(List<Media> caches) {
        heapCache.putAll(caches);
    }

    @Override
    public Media get(int index) {
        return heapCache.get(index);
    }

    @Override
    public List<Media> get(int start, int end) {
        return heapCache.get(start, end);
    }

    @Override
    public List<Media> get() {
        return heapCache.get();
    }

    @Override
    public void remove(int index) {
        heapCache.remove(index);
    }

    @Override
    public void remove(Media o) {
        heapCache.remove(o);
    }

    @Override
    public void clear() {
        heapCache.clear();
    }

    @Override
    public BiPredicate<Media, Media> comparator() {
        return heapCache.comparator();
    }
}
