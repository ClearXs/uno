package cc.allio.uno.test;

import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.MybatisPlusEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 给予测试类上，扫描那些被{@link Service}标识的类注册进入Spring中
 *
 * @author jiangwei
 * @date 2022/10/28 16:28
 * @since 1.1.0
 * @deprecated 自1.1.4版本之后删除，使用cc.allio.uno.test.env.anno包下的注解构建环境
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RunTest(envs = {
        @RunTest.Environment(env = DataSourceEnvironment.class),
        @RunTest.Environment(env = MybatisPlusEnvironment.class)}, components = RunServiceTest.Scan.class)
@Deprecated
public @interface RunServiceTest {

    /**
     * 提供用于扫描MapperScan的配置类Class对象
     *
     * @return class对象
     */
    Class<?> mapperScan();

    @ComponentScan(basePackages = "cc.allio.**.service.**", includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class))
    class Scan {

    }
}
