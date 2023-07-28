package cc.allio.uno.test.env;

import cc.allio.uno.test.CoreTest;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.lang.annotation.Annotation;

/**
 * Jpa环境测试类
 *
 * @author jiangwei
 * @date 2022/11/29 11:55
 * @since 1.1.2
 */
public class JpaEnvironment extends VisitorEnvironment {

    @Override
    protected void onSupport(CoreTest coreTest) throws Throwable {
        coreTest.registerAutoConfiguration(JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class, ExtraConfiguration.class);
    }

    @Override
    public Class<? extends Annotation>[] getPropertiesAnnotation() {
        return null;
    }

    @AutoConfigurationPackage
    static class ExtraConfiguration {

    }
}
