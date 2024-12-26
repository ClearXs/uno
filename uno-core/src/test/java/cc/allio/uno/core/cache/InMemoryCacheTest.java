package cc.allio.uno.core.cache;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * 内容缓存测试
 *
 * @author j.x
 * @since 1.0
 */
class InMemoryCacheTest extends BaseTestCase {

    private Cache<String> cache;

    @Override
    protected void onInit() throws Throwable {
        cache = new InMemoryCache<>();
    }

    /**
     * Test Case:测试放入缓存
     */
    @Test
    void testPut() {
        String content = "123";
        String put = cache.put("123");
        assertEquals(put, content);
    }

    @Test
    void testPutWithNull() {
        assertNull(cache.put(null));
    }
}
