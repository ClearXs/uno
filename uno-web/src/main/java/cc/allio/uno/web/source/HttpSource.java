package cc.allio.uno.web.source;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.lang.reflect.Method;

/**
 * http source的抽象接口，包含注册端点方法
 *
 * @author j.x
 * @date 2023/5/15 12:30
 * @since 1.1.4
 */
public interface HttpSource {

    /**
     * 注册
     *
     * @param context  spring application context
     * @param endpoint 端点方法
     * @param parser   路径解析器
     */
    default void registryEndpoint(ApplicationContext context, Method endpoint, PathPatternParser parser) {
        RequestMappingHandlerMapping handlerMapping = null;
        try {
            handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        } catch (NoSuchBeanDefinitionException ex) {
            LoggerFactory.getLogger(getClass()).error("register http source {} failed", getMappingUrl(), ex);
        }
        if (handlerMapping != null) {
            Method requestMethod = endpoint;
            PathPattern pathPattern = parser.parse(getMappingUrl());
            PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(pathPattern.getPatternString());
            RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(RequestMethod.POST);
            RequestMappingInfo mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
            handlerMapping.registerMapping(mappingInfo, this, requestMethod);
        }
    }

    /**
     * 获取映射地址
     *
     * @return 地址 /xx/xx
     */
    String getMappingUrl();
}
