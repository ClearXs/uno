package cc.allio.uno.test;

import cc.allio.uno.test.env.Visitor;
import cc.allio.uno.test.env.annotation.ImportAutoConfiguration;
import cc.allio.uno.test.listener.Listener;
import cc.allio.uno.test.runner.Runner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.*;

/**
 * 运行的测试环境
 *
 * @author j.x
 * @see RunTestAttributes
 * @see TestManager
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@ExtendWith(UnoExtension.class)
@ImportAutoConfiguration
public @interface RunTest {

    /**
     * 配置文件名称
     */
    String profile() default "uno";

    /**
     * 配置文件对应环境
     */
    String active() default "test";

    /**
     * 提供spring的组件，使其注册为Spring-Bean
     *
     * @return 被spring @Component注解的class对象
     */
    Class<?>[] components() default {};

    /**
     * string类型的key-value对。如{server.port=222}。放入spring environment
     */
    String[] properties() default {};

    /**
     * 根据给定的参数判断是否创建Web应用
     */
    WebEnvironment webEnvironment() default WebEnvironment.NONE;

    /**
     * 提供在spring环境中测试不同阶段的回调
     *
     * @return Runner
     */
    Class<? extends Runner>[] runner() default {};

    /**
     * 环境构建时，提供拓展能力。
     *
     * @return Visitor
     */
    Class<? extends Visitor>[] visitor() default {};

    /**
     * 提供Test Listener
     *
     * @return TestListener
     */
    Class<? extends Listener>[] listeners() default {};

    /**
     * 参考于SpringBootTest WebEnvironment
     */
    enum WebEnvironment {

        /**
         * Creates a {@link WebApplicationContext} with a mock servlet environment if
         * servlet APIs are on the classpath, a {@link ReactiveWebApplicationContext} if
         * Spring WebFlux is on the classpath or a regular {@link ApplicationContext}
         * otherwise.
         */
        MOCK(false),

        /**
         * Creates a web application context (reactive or servlet based) and sets a
         * {@code server.port=0} {@link org.springframework.core.env.Environment} property (which usually triggers
         * listening on a random port). Often used in conjunction with a
         */
        RANDOM_PORT(true),

        /**
         * Creates a (reactive) web application context without defining any
         * {@code server.port=0} {@link org.springframework.core.env.Environment} property.
         */
        DEFINED_PORT(true),

        /**
         * Creates an {@link ApplicationContext} and sets
         * {@link SpringApplication#setWebApplicationType(WebApplicationType)} to
         * {@link WebApplicationType#NONE}.
         */
        NONE(false);

        private final boolean embedded;

        WebEnvironment(boolean embedded) {
            this.embedded = embedded;
        }

        /**
         * Return if the environment uses an {@link ServletWebServerApplicationContext}.
         *
         * @return if an {@link ServletWebServerApplicationContext} is used.
         */
        public boolean isEmbedded() {
            return this.embedded;
        }
    }

}
