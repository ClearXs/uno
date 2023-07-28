package cc.allio.uno.test.feign;

import cc.allio.uno.test.env.FeignEnvironment;
import cc.allio.uno.test.runner.Running;
import cc.allio.uno.test.RunTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.AliasFor;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactivefeign.spring.config.ReactiveFeignClient;

import java.lang.annotation.*;

/**
 * 参考自{@link EnableFeignClients}与{@link EnableReactiveFeignClients}
 *
 * @author jiangwei
 * @date 2022/10/28 16:09
 * @since 1.1.0
 * @deprecated 在1.1.4版本之后删除
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Running(FeignRunner.class)
@RunTest(envs = @RunTest.Environment(env = FeignEnvironment.class))
@Deprecated
public @interface RunFeignTest {

    /**
     * {@link #basePackages()}别名
     *
     * @return 数组
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * 扫描被{@link FeignClient}与{@link ReactiveFeignClient}标识的接口
     *
     * @return 数组
     */
    String[] basePackages() default {};

    /**
     * 给予被{@link FeignClient}与{@link ReactiveFeignClient}标识的Component
     *
     * @return component-class对象
     */
    Class<?>[] basePackagesClasses() default {};

    /**
     * 指定Client class对象
     *
     * @return client 数组
     */
    Class<?>[] clients() default {};

}
