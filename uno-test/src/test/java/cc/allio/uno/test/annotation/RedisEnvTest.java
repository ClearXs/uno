package cc.allio.uno.test.annotation;

import cc.allio.uno.test.*;
import cc.allio.uno.test.env.annotation.RedisEnv;
import cc.allio.uno.test.env.annotation.properties.RedisProperties;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

@RunTest
@RedisEnv
@RedisProperties(host = "192.168.2.29", password = "123456")
public class RedisEnvTest extends BaseTestCase {

    @Inject
    private StringRedisTemplate redisTemplate;

    @Test
    void testGetAndPut(@Parameter CoreTest coreTest) {
        assertNotNull(redisTemplate);
        redisTemplate.opsForValue().set("s", "test");
        Object r = redisTemplate.opsForValue().get("s");
        assertEquals("test", r);
    }
}
