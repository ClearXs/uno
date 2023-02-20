package cc.allio.uno.test;

import cc.allio.uno.test.env.TestSpringEnvironment;
import cc.allio.uno.test.runner.CoreTestRunner;
import cc.allio.uno.test.runner.Running;

import java.lang.annotation.*;

/**
 * 运行的测试环境
 *
 * @author jiangwei
 * @date 2022/9/15 17:39
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Running(CoreTestRunner.class)
public @interface RunTest {

    /**
     * 配置文件名称
     *
     * @return
     */
    String profile() default "uno";

    /**
     * 配置文件对应环境
     *
     * @return
     */
    String active() default "test";

    /**
     * 提供测试运行的环境
     *
     * @return
     */
    Environment[] envs() default {};

    /**
     * 提供Spring的组件，使其注册为Spring-Bean
     *
     * @return
     */
    Class<?>[] components() default {};

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Environment {

        /**
         * {@link TestSpringEnvironment}的Class对象，用于进行实例化
         *
         * @return
         */
        Class<? extends TestSpringEnvironment> env();

        /**
         * 用于实例化{@link TestSpringEnvironment}的Class对象时构造器器的参数列表的全限定类名，根据该参数进行传参数
         *
         * @return
         */
        Class<?>[] classArgs() default {};
    }

}
