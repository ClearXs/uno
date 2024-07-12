package cc.allio.uno.test.env;

import cc.allio.uno.test.env.annotation.properties.DataSourceProperties;
import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

import javax.annotation.Priority;
import java.lang.annotation.Annotation;

/**
 * 数据源测试环境类
 *
 * @author j.x
 * @date 2022/2/14 14:11
 * @since 1.0
 */
@Priority(Integer.MIN_VALUE)
public class DataSourceEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[]{DataSourceProperties.class};
    }

}
