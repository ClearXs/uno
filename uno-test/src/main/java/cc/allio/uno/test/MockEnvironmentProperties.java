package cc.allio.uno.test;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * 模拟Test环境中配置
 *
 * @author jiangwei
 * @date 2022/2/27 22:28
 * @since 1.0
 */
public class MockEnvironmentProperties {

    private MockEnvironmentProperties() {
    }

    public static RedisProperties mockRedisProperties() {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost("localhost");
        redisProperties.setPort(6379);
        redisProperties.setDatabase(0);
        return redisProperties;
    }

    public static MybatisPlusProperties mockMybatisPlusProperties() {
        MybatisPlusProperties mybatisPlusProperties = new MybatisPlusProperties();
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setLogImpl(StdOutImpl.class);
        mybatisPlusProperties.setConfiguration(mybatisConfiguration);
        return mybatisPlusProperties;
    }

    /**
     * <pre><code>
     * dataSourceProperties.setDriverClassName("org.postgresql.Driver");
     * dataSourceProperties.setUrl("jdbc:postgresql://localhost:5432/test?stringtype=unspecified");
     * dataSourceProperties.setUsername("postgres");
     * dataSourceProperties.setPassword("123456");
     * </code></pre>
     * <pre><code>
     * dataSourceProperties.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
     * dataSourceProperties.setUsername("root");
     * dataSourceProperties.setPassword("123456");
     * dataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
     * </code></pre>
     * <pre><code>
     * dataSourceProperties.setDriverClassName("org.h2.Driver");
     * dataSourceProperties.setUrl("jdbc:h2:mem:test-db;IGNORECASE=TRUE");
     * </code></pre>
     *
     * @return
     */
    public static DataSourceProperties mockDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        dataSourceProperties.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceProperties.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&tinyInt1isBit=false&allowMultiQueries=true&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true");
        dataSourceProperties.setUsername("root");
        dataSourceProperties.setPassword("123456");
        return dataSourceProperties;
    }

}
