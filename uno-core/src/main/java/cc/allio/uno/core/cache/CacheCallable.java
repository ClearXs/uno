package cc.allio.uno.core.cache;

import java.util.List;

/**
 * 缓存回调
 *
 * @author jiangwei
 * @date 2022/2/9 15:53
 * @since 1.0
 */
@FunctionalInterface
public interface CacheCallable<T> {

    /**
     * 有执行者调用，提供缓存数据
     *
     * @param cache  缓存对象
     * @param buffer 缓存集合
     */
    void call(Cache<T> cache, List<T> buffer);
}
