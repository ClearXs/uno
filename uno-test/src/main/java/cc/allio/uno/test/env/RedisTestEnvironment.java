package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

/**
 * Redis测试环境类
 *
 * @author jiangwei
 * @date 2022/2/14 14:20
 * @since 1.0
 */
public class RedisTestEnvironment implements TestSpringEnvironment {

    static final RedisProperties DEFAULT_REDIS_PROPERTIES = new RedisProperties();
    final RedisProperties redisProperties;

    public RedisTestEnvironment() {
        this(DEFAULT_REDIS_PROPERTIES);
    }

    public RedisTestEnvironment(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Override
    public void support(BaseCoreTest test) {
        test.addProperty("spring.redis.host", test.getProperty("spring.redis.host", redisProperties.getHost()));
        test.addProperty("spring.redis.port", test.getProperty("spring.redis.port", String.valueOf(redisProperties.getPort())));
        test.addProperty("spring.redis.password", test.getProperty("spring.redis.password", redisProperties.getPassword()));
        test.addProperty("spring.redis.database", test.getProperty("spring.redis.database", String.valueOf(redisProperties.getDatabase())));
        test.registerComponent(RedisAutoConfiguration.class);
    }

}
