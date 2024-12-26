package cc.allio.uno.test.listener;

import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.test.RunTest;
import cc.allio.uno.test.TestContext;
import jakarta.annotation.Priority;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.util.ClassUtils;

import static cc.allio.uno.test.TestContext.WEB_SERVER;

/**
 * 设计思路参考于SpringBootTestContextBootstrapper
 *
 * @author j.x
 * @since 1.1.4
 */
@Priority(Integer.MIN_VALUE)
public class WebListener implements Listener {

    private static final String[] WEB_ENVIRONMENT_CLASSES = {"jakarta.servlet.Servlet",
            "org.springframework.web.context.ConfigurableWebApplicationContext"};

    private static final String REACTIVE_WEB_ENVIRONMENT_CLASS = "org.springframework."
            + "web.reactive.DispatcherHandler";

    private static final String MVC_WEB_ENVIRONMENT_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    private static final String JERSEY_WEB_ENVIRONMENT_CLASS = "org.glassfish.jersey.server.ResourceConfig";

    @Override
    public void beforeEntryClass(TestContext testContext) throws Exception {
        prepareWebApplication(testContext);
        RunTest.WebEnvironment webEnvironment = getWebEnvironment(testContext);
        WebApplicationType webApplicationType = getWebApplicationType(testContext);
        if (webApplicationType == WebApplicationType.SERVLET
                && (webEnvironment.isEmbedded() || webEnvironment == RunTest.WebEnvironment.MOCK)) {
            testContext.putAttribute(WEB_SERVER, "servlet");
        } else if (webApplicationType == WebApplicationType.REACTIVE
                && (webEnvironment.isEmbedded() || webEnvironment == RunTest.WebEnvironment.MOCK)) {
            testContext.putAttribute(WEB_SERVER, "reactive");
        } else {
            testContext.putAttribute(WEB_SERVER, "none");
        }
    }

    /**
     * 准备处理web应用
     *
     * @param context test context
     */
    private void prepareWebApplication(TestContext context) {
        RunTest.WebEnvironment webEnvironment = getWebEnvironment(context);
        // 参数的设置
        if (webEnvironment == RunTest.WebEnvironment.RANDOM_PORT) {
            context.getRunTestAttributes().addInlineProperties("server.port=0");
        }
        if (webEnvironment == RunTest.WebEnvironment.NONE) {
            context.getRunTestAttributes().addInlineProperties("spring.main.web-application-type=none");
        }

        // 判断是否需要激活ServletTestExecutionListener
        if (webEnvironment == RunTest.WebEnvironment.MOCK && classpathWebApplicationType() == WebApplicationType.SERVLET) {
            context.getRunTestAttributes().addInlineProperties("org.springframework.test.context.web.ServletTestExecutionListener.activateListener=true");
        } else if (webEnvironment.isEmbedded()) {
            context.getRunTestAttributes().addInlineProperties("org.springframework.test.context.web.ServletTestExecutionListener.activateListener=false");
        }
    }

    public WebApplicationType getWebApplicationType(TestContext context) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(
                TestPropertySourceUtils.convertInlinedPropertiesToMap(StringUtils.toStringArray(context.getRunTestAttributes().getInlineProperties())));
        Binder binder = new Binder(source);
        return binder.bind("spring.main.web-application-type", Bindable.of(WebApplicationType.class))
                .orElseGet(this::classpathWebApplicationType);
    }

    /**
     * 判断类路径下是否包含一些指定的类，根据这些判断返回什么类型的web应用{@link WebApplicationType}
     *
     * @return WebApplicationType
     */
    private WebApplicationType classpathWebApplicationType() {
        if (ClassUtils.isPresent(REACTIVE_WEB_ENVIRONMENT_CLASS, null)
                && !ClassUtils.isPresent(MVC_WEB_ENVIRONMENT_CLASS, null)
                && !ClassUtils.isPresent(JERSEY_WEB_ENVIRONMENT_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        for (String className : WEB_ENVIRONMENT_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        return WebApplicationType.SERVLET;
    }

    private RunTest.WebEnvironment getWebEnvironment(TestContext testContext) {
        return testContext.getRunTestAttributes().getWebEnvironment();
    }
}
