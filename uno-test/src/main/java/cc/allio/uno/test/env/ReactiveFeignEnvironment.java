package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import reactivefeign.spring.config.ReactiveFeignAutoConfiguration;
import reactivefeign.spring.config.ReactiveFeignClientsConfiguration;

import java.lang.annotation.Annotation;

/**
 * Reactive Feign Environment
 *
 * @author jiangwei
 * @date 2023/3/9 11:54
 * @since 1.1.4
 */
public class ReactiveFeignEnvironment extends VisitorEnvironment {
    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[0];
    }

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(
                // reactive configuration
                ReactiveFeignAutoConfiguration.class,
                ReactiveFeignClientsConfiguration.class,
                // Webclient$Builder
                WebClientAutoConfiguration.class
        );
    }
}
