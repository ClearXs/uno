package cc.allio.uno.starter.redis;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.starter.redis.config.ReactiveRedisAutoConfiguration;
import cc.allio.uno.test.BaseCoreTest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.test.StepVerifier;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

class ReactiveRedisTemplateTest extends BaseCoreTest {
    @Override
    protected void onEnvBuild() {
        registerComponent(
                ReactiveRedisAutoConfiguration.class);

        ObjectMapper mapper = new ObjectMapper();
        // 设置地点为中国
        mapper.setLocale(Locale.CHINA);
        // 设置为中国上海时区
        mapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        //序列化时，日期的统一格式
        mapper.setDateFormat(new SimpleDateFormat(DateUtil.PATTERN_DATETIME, Locale.CHINA));
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
        mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        mapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        // 失败处理
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 反序列化时，属性不存在的兼容处理s
        mapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 单引号处理
        mapper.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
        register(ObjectMapper.class, mapper);
    }

    @Test
    void testValueOperation() {
        ReactiveRedisTemplateFactory factory = getBean(ReactiveRedisTemplateFactory.class);
        ReactiveRedisTemplate<String, String> template = factory.genericTemplate(String.class);
        template.opsForValue()
                .set("test", "test")
                .then(template.opsForValue().get("test"))
                .as(StepVerifier::create)
                .expectNext("test")
                .verifyComplete();

    }

    @Test
    void testListOperation() {
        ReactiveRedisTemplateFactory factory = getBean(ReactiveRedisTemplateFactory.class);
        ReactiveRedisTemplate<String, User> template = factory.genericTemplate(User.class);
        template.opsForList()
                .leftPush("users", new User("name"))
                .then(template.opsForList().leftPop("users"))
                .as(StepVerifier::create)
                .expectNext(new User("name"))
                .verifyComplete();
    }

    @Override
    protected void onRefreshComplete() throws Throwable {

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
    }
}
