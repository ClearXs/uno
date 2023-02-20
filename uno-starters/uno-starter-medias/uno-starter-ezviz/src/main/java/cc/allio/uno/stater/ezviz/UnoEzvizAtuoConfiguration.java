package cc.allio.uno.stater.ezviz;

import cc.allio.uno.component.media.MediaCache;
import cc.allio.uno.component.media.command.CommandController;
import cc.allio.uno.stater.ezviz.command.EzvizCommandController;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author heitianzhen
 * @date 2022/4/12 11:30
 */
@Configuration
@EnableConfigurationProperties(EzvizMediaProperties.class)
public class UnoEzvizAtuoConfiguration {

    @Bean
    public CommandController ezvizCommandController(EzvizMediaProperties mediaProperties) {
        return new EzvizCommandController(mediaProperties);
    }

    @Bean
    public MediaCache mediaCache(StringRedisTemplate redisTemplate) {
        return new MediaCache(redisTemplate);
    }

}
