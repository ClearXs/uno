package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

/**
 * 订阅信息单元测试
 *
 * @author j.x
 * @date 2022/2/7 17:53
 * @since 1.0
 */
class SubscriptionTest extends BaseTestCase {

    MultiValueMap<String, String> customized;

    @Override
    protected void onInit() throws Throwable {
        customized = new LinkedMultiValueMap<>();
    }

    /**
     * Test Cast: 测试自定义配置构建订阅信息
     */
    @Test
    void testCustomizePropertiesWithSubscription() {
        customized.put("test-group", Arrays.asList("test1", "test2"));
        List<Subscription> subscriptions = Subscription.buildSubscriptionByCustomizeProperties(customized, "test-group", "");
        assertEquals(2, subscriptions.size());
    }

    /**
     * Test Case: 测试自定义配置为空时返回空集合
     */
    @Test
    void testCustomizeIsEmptyWithSubscription() {
        List<Subscription> empty = Subscription.buildSubscriptionByCustomizeProperties(customized, "empty", "");
        assertNotNull(empty);
        assertEquals(0, empty.size());
    }

}
