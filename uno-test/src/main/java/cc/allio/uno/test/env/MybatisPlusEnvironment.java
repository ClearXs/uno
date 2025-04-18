package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import cc.allio.uno.test.env.annotation.properties.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import jakarta.annotation.Priority;

import java.lang.annotation.Annotation;

/**
 * Mybatis-plus测试环境类
 *
 * @author j.x
 * @since 1.0
 */
@Priority(Integer.MAX_VALUE)
public class MybatisPlusEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(MybatisPlusAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{MybatisPlusProperties.class};
    }

}
