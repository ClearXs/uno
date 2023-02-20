package cc.allio.uno.uno.starter.ezviz.command;

import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.stater.ezviz.EzvizAccessToken;
import cc.allio.uno.stater.ezviz.EzvizMediaProperties;
import cc.allio.uno.stater.ezviz.command.EzvizCommandController;
import cc.allio.uno.test.BaseCoreTest;
import cc.allio.uno.test.MockEnvironmentProperties;
import cc.allio.uno.test.env.RedisTestEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironment;
import cc.allio.uno.test.env.TestSpringEnvironmentFacade;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;

/**
 * @author heitianzhen
 * @date 2022/4/19 14:56
 */
class EzvizMediaListCommandTest extends BaseCoreTest {

    /**
     * Test Case: 测试是否能够从萤火虫获取Token
     */
    @Test
    void testGetAccessToken() throws MediaException, InterruptedException {
        EzvizMediaProperties unoMediaProperties = new EzvizMediaProperties();
        HashMap<String, Object> properties = Maps.newHashMap();
        properties.put(CommandContext.MEDIA_PROPERTY, unoMediaProperties);
        CommandContext context = new CommandContext(null, properties);
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("101.201.220.209");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setPassword("Automic@123");
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        stringRedisTemplate.afterPropertiesSet();
        EzvizAccessToken ezvizAccessToken = new EzvizAccessToken(stringRedisTemplate);
        String accessTokenFromRedis = ezvizAccessToken.getAccessTokenFromRedis(context);
        System.out.println(accessTokenFromRedis);
    }

    @Override
    public TestSpringEnvironment supportEnv() {
        return new TestSpringEnvironmentFacade(
                new RedisTestEnvironment(MockEnvironmentProperties.mockRedisProperties())
        );
    }

    @Override
    protected void onEnvBuild() {

    }

    @Override
    protected void onRefreshComplete() throws Throwable {
        EzvizMediaProperties unoMediaProperties = new EzvizMediaProperties();
        registerComponent(EzvizCommandController.class, MediaCache.class);
        register(EzvizMediaProperties.class, unoMediaProperties);
    }

    @Override
    protected void onContextClose() throws Throwable {

    }
}
