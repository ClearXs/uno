package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.RedisProperties;
import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * Redis测试环境类
 *
 * @author j.x
 * @since 1.0
 */
public class RedisEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(RedisAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{RedisProperties.class};
    }
}
