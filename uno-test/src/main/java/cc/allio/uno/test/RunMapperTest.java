package cc.allio.uno.test;

import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.MybatisEnvironment;
import cc.allio.uno.test.env.MybatisPlusEnvironment;

import java.lang.annotation.*;

/**
 * 给予测试类中，扫描那些Mapper的类
 *
 * @author jiangwei
 * @date 2022/10/28 16:30
 * @since 1.1.0
 * @deprecated 自1.1.4版本之后删除，使用cc.allio.uno.test.env.anno包下的注解构建环境
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RunTest(envs = {
        @RunTest.Environment(env = DataSourceEnvironment.class),
        @RunTest.Environment(env = MybatisPlusEnvironment.class),
        @RunTest.Environment(env = MybatisEnvironment.class)})
@Deprecated
public @interface RunMapperTest {

    /**
     * 提供用于扫描MapperScan的配置类Class对象
     *
     * @return class对象
     */
    Class<?> mapperScan();
}
