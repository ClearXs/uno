package cc.allio.uno.test;

import cc.allio.uno.test.env.DatasourceTestEnvironment;
import cc.allio.uno.test.env.MybatisPlusTestEnvironment;
import cc.allio.uno.test.env.MybatisTestEnvironment;

import java.lang.annotation.*;

/**
 * 给予测试类中，扫描那些Mapper的类
 *
 * @author jiangwei
 * @date 2022/10/28 16:30
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RunTest(envs = {
        @RunTest.Environment(env = DatasourceTestEnvironment.class),
        @RunTest.Environment(env = MybatisPlusTestEnvironment.class),
        @RunTest.Environment(env = MybatisTestEnvironment.class)})
public @interface RunMapperTest {

    /**
     * 提供用于扫描MapperScan的配置类Class对象
     *
     * @return class对象
     */
    Class<?> mapperScan();
}
