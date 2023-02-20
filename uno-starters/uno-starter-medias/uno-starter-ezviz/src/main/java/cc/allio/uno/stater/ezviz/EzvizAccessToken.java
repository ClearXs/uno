package cc.allio.uno.stater.ezviz;

import cc.allio.uno.component.http.metadata.HttpSwapper;
import cc.allio.uno.component.media.CommandContext;
import cc.allio.uno.component.media.MediaException;
import cc.allio.uno.component.media.MediaProperty;
import cc.allio.uno.core.cache.CacheKey;
import cc.allio.uno.stater.ezviz.command.EzvizHttpResponseAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author heitianzhen
 * @date 2022/4/18 20:32
 */
@Slf4j
@AllArgsConstructor
@Component
public class EzvizAccessToken {
    public EzvizAccessToken(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    private StringRedisTemplate redisTemplate;

    private String tokenKey = CacheKey.COMPANY_PREFIX + ":ezvizToken";

    private final Object lock = new Object();

    public Mono<Void> getAccessTokenFromEzviz(CommandContext context){
//        redisTemplate.opsForValue().set(tokenKey,"at.7bvj5q3w4t8o1qny69xybjnw2c3s1m89-67wgfzutyj-0rj8t73-kvbduoe7t",30, TimeUnit.SECONDS);
        EzvizMediaProperties property = (EzvizMediaProperties)context.getOrThrows("MEDIA_PROPERTY", MediaProperty.class);
        return (new EzvizHttpResponseAdapter(HttpSwapper.build(property.getOAuth().getAccessTokenUrl(), HttpMethod.POST)
                .addParameter("appKey",property.getOAuth().getAppKey())
                .addParameter("appSecret",property.getOAuth().getAppSecret())
                .swap())).testResponse().flatMap((token) -> {
            String accessToken = token.get("accessToken").textValue();
            try {
                synchronized (lock) {
                    if (accessToken != null) {
                        redisTemplate.opsForValue().set(tokenKey,accessToken,7, TimeUnit.DAYS);
                    }
                }
            } catch (Exception e) {
                log.error("Put into Redis cache failed", e);
            }
            return Mono.empty();
        });
    }

    public String getAccessTokenFromRedis(CommandContext context){
        String accessToken = redisTemplate.opsForValue().get(tokenKey);
        if(StringUtils.isBlank(accessToken)){
            getAccessTokenFromEzviz(context).subscribe();
            accessToken = redisTemplate.opsForValue().get(tokenKey);
            if(StringUtils.isBlank(accessToken)){
                throw new MediaException("Failed to obtain token");
            }
        }
        return accessToken;
    }
}
