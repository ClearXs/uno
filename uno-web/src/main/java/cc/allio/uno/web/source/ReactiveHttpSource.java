package cc.allio.uno.web.source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 基于{@link RequestMappingHandlerMapping}的HTTP数据源
 *
 * @author j.x
 * @since 1.1.2
 */
@Slf4j
public class ReactiveHttpSource extends BaseHttpSource {

    public ReactiveHttpSource(String requestMappingName) {
        super(requestMappingName);
    }

    @Override
    public void register(ApplicationContext context) {
        RequestMappingHandlerMapping handlerMapping = null;
        try {
            handlerMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("register reactive http source {} failed", requestMappingUrl, ex);
        }
        if (handlerMapping != null) {
            Method requestMethod = getEndpointMethod();
            RequestMappingInfo mappingInfo = RequestMappingInfo.paths(requestMappingUrl).methods(RequestMethod.POST).build();
            handlerMapping.registerMapping(mappingInfo, this, requestMethod);
        }
    }
}
