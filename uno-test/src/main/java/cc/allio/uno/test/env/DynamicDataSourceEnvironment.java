package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DruidDynamicDataSourceConfiguration;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import jakarta.annotation.Priority;

import java.lang.annotation.Annotation;

@Priority(Integer.MIN_VALUE + 1)
public class DynamicDataSourceEnvironment extends VisitorEnvironment {
    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return new Class[0];
    }

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(
                DynamicDataSourceAutoConfiguration.class,
                DynamicDataSourceAutoConfiguration.class,
                DruidDynamicDataSourceConfiguration.class);
    }
}
