package cc.allio.uno.web.source;

import cc.allio.uno.core.StringPool;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 基于{@link RequestMappingHandlerMapping}的数据源
 *
 * @author jiangwei
 * @date 2022/11/24 16:54
 * @since 1.1.2
 */
public class WebHttpSource extends BaseHttpSource {

    public WebHttpSource(String requestMappingName) {
        super(requestMappingName);
    }

    @Override
    public void register(ApplicationContext context) {
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Method requestMethod = ReflectionUtils.findMethod(BaseHttpSource.class, ENDPOINT, Map.class);
        PathPattern pathPattern = parser.parse(requestMappingName + StringPool.SLASH + ENDPOINT);
        PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition(pathPattern.getPatternString());
        RequestMethodsRequestCondition requestMethodsRequestCondition = new RequestMethodsRequestCondition(RequestMethod.POST);
        RequestMappingInfo mappingInfo = new RequestMappingInfo(patternsRequestCondition, requestMethodsRequestCondition, null, null, null, null, null);
        handlerMapping.registerMapping(mappingInfo, this, requestMethod);
    }
}
