package cc.allio.uno.web.source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.condition.RequestMethodsRequestCondition;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;

/**
 * 基于{@link RequestMappingHandlerMapping}的HTTP数据源
 *
 * @author jiangwei
 * @date 2022/11/24 16:54
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
            handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("register reactive http source {} failed", requestMappingUrl, ex);
        }
        if (handlerMapping != null) {
            Method requestMethod = getEndpointMethod();
            PathPattern pathPattern = parser.parse(requestMappingUrl);
            PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(pathPattern);
            RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(RequestMethod.POST);
            RequestMappingInfo mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
            handlerMapping.registerMapping(mappingInfo, this, requestMethod);
        }
    }
}
