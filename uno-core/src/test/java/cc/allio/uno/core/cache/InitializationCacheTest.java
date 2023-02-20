package cc.allio.uno.core.cache;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.User;
import org.junit.jupiter.api.Test;

/**
 * 测试进行初始化设置的缓存
 *
 * @author jiangwei
 * @date 2022/2/10 13:06
 * @since 1.0
 */
class InitializationCacheTest extends BaseTestCase {

    private Cache<User> delegate;

    private User mock;

    @Override
    protected void onInit() throws Throwable {
        delegate = new InMemoryCache<>();
        mock = new User("1", "jojo", "");
    }

    /**
     * Test Case:测试初始回调
     */
    @Test
    void testInitialCallback() {
        delegate.put(mock);
        new InitializationCache<>(delegate, (cache, buffer) -> {
            assertEquals(1, buffer.size());
            assertEquals(mock, buffer.get(0));
        });
    }

    @Override
    protected void onDown() throws Throwable {

    }
}
