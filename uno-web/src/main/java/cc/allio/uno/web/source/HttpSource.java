package cc.allio.uno.web.source;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * http source的抽象接口，包含注册端点方法
 *
 * @author j.x
 * @since 1.1.4
 */
public interface HttpSource {

    /**
     * 注册
     *
     * @param context         spring application context
     * @param endpointHandler 端点方法
     */
    default void registryEndpoint(ApplicationContext context, Method endpointHandler) {
        RequestMappingHandlerMapping handlerMapping = null;
        try {
            handlerMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        } catch (NoSuchBeanDefinitionException ex) {
            LoggerFactory.getLogger(getClass()).error("register http source {} failed", getMappingUrl(), ex);
        }
        if (handlerMapping != null) {
            String mappingUrl = getMappingUrl();
            RequestMappingInfo mappingInfo = RequestMappingInfo.paths(mappingUrl).methods(RequestMethod.POST).build();
            handlerMapping.registerMapping(mappingInfo, this, endpointHandler);
        }
    }

    /**
     * 获取映射地址
     *
     * @return 地址 /xx/xx
     */
    String getMappingUrl();
}
