package cc.allio.uno.data.query.db;

import cc.allio.uno.data.query.db.query.interceptor.QueryInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * UNO-DATA配置
 *
 * @author j.x
 * @since 1.1.2
 */
@Configuration(proxyBeanMethods = false)
@EnableAspectJAutoProxy
public class UnoDataMybatisAutoConfiguration  {

    @Bean
    public QueryInterceptor unoQueryInterceptor() {
        return new QueryInterceptor();
    }

}
