package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.MybatisProperties;
import cc.allio.uno.test.CoreTest;
import jakarta.annotation.Priority;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * Mybatis测试环境类。
 * <p><b>优先级需要低于{@link DataSourceEnvironment}</b></p>
 *
 * @author jiangwei
 * @date 2022/2/14 14:27
 * @since 1.0
 */
@Priority(0)
public class MybatisEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(
                MybatisAutoConfiguration.class,
                MybatisLanguageDriverAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{MybatisProperties.class};
    }

}
