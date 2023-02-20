package cc.allio.uno.data.mybatis;

import cc.allio.uno.data.mybatis.injector.QueryList;
import cc.allio.uno.data.mybatis.query.interceptor.QueryInterceptor;
import cc.allio.uno.data.mybatis.type.DateDimensionTypeHandler;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

/**
 * UNO-DATA配置
 *
 * @author jiangwei
 * @date 2022/11/22 15:11
 * @since 1.1.2
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MybatisPlusAutoConfiguration.class, UnoDataMybatisAutoConfiguration.class})
@EnableAspectJAutoProxy
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
public class UnoDataMybatisAutoConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Bean
    public QueryList unoQueryList() {
        return new QueryList();
    }

    @Bean
    public QueryInterceptor unoQueryInterceptor() {
        return new QueryInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ISqlInjector sqlInjector(List<AbstractMethod> otherMethod) {
        return new UnoSqlInjector(otherMethod);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        MybatisPlusProperties properties = applicationContext.getBean(MybatisPlusProperties.class);
        com.baomidou.mybatisplus.core.MybatisConfiguration configuration = properties.getConfiguration();
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register(DateDimensionTypeHandler.class);
    }
}
