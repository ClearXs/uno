package cc.allio.uno.http.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 基于{@link RequestMappingHandlerMapping}的数据源
 *
 * @author j.x
 * @since 1.1.2
 */
@Slf4j
public class WebHttpSource extends BaseHttpSource {

    public WebHttpSource(String requestMappingName) {
        super(requestMappingName);
    }

    @Override
    public void register(ApplicationContext context) {
        registryEndpoint(context, getEndpointMethod());
    }
}
