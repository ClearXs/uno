package cc.allio.uno.data.query.support.config;

import cc.allio.uno.data.query.mybatis.injector.QueryList;
import cc.allio.uno.data.query.mybatis.injector.QuerySqlInjector;
import cc.allio.uno.data.query.mybatis.query.interceptor.QueryInterceptor;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MybatisConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext context;

    @Bean
    public PostgreKeyGenerator postgreKeyGenerator() {
        return new PostgreKeyGenerator();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;
    }

    @Bean
    public QueryInterceptor queryInterceptor() {
        return new QueryInterceptor();
    }

    @Bean
    public QueryList queryList() {
        return new QueryList();
    }

    @Bean
    public QuerySqlInjector querySqlInjector(List<AbstractMethod> otherMethod) {
        return new QuerySqlInjector(otherMethod);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MybatisPlusProperties properties = context.getBean(MybatisPlusProperties.class);
        com.baomidou.mybatisplus.core.MybatisConfiguration configuration = properties.getConfiguration();
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        registry.register("cc.allio.uno.componment.mybatis.type");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
