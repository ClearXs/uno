package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.annotation.properties.ServerProperties;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * servlet web 环境
 *
 * @author jiangwei
 * @date 2023/3/9 20:11
 * @since 1.1.4
 */
public class ServletWebEnvironment extends VisitorEnvironment {
    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{ServerProperties.class};
    }

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        // 构建web 环境
        coreTest.registerAutoConfiguration(
                HttpMessageConvertersAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class,
                ServletWebServerFactoryAutoConfiguration.class);
    }
}
