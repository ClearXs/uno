package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * Jpa环境测试类
 *
 * @author jiangwei
 * @date 2022/11/29 11:55
 * @since 1.1.2
 */
public class JpaTestEnvironment implements TestSpringEnvironment {
    @Override
    public void support(BaseCoreTest test) {
        test.registerComponent(JpaRepositoriesAutoConfiguration.class, HibernateJpaAutoConfiguration.class, ExtraConfiguration.class);
    }

    @AutoConfigurationPackage
    static class ExtraConfiguration {

    }
}
