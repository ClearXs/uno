package cc.allio.uno.starter.liquibase.config;

import cc.allio.uno.starter.liquibase.DataSourceAdapterDispatcher;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Blade-Liquibase自动配置类。</br>
 * <ol>
 *     <li>扫描在/db/migrations/#{dbType}目录下是否存在db_migration格式的文件</li>
 *     <li>代码逻辑将会根据当前服务数据源的配置创建Liquibase对象。</li>
 *     <li>如果当前项目配置多数据源，需要在db_migration-#{dynamic}。以此适配多数据源</li>
 * </ol>
 *
 * @author jiangwei
 * @date 2022/1/19 10:47
 * @since 1.0
 */
@Slf4j
@EnableAutoConfiguration
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", havingValue = "true")
@AutoConfigureBefore({LiquibaseAutoConfiguration.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UnoLiquibaseAutoConfiguration implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        Map<String, DataSource> dataSourceMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, DataSource.class);
        if (CollectionUtils.isEmpty(dataSourceMap)) {
            return;
        }
        if (dataSourceMap.size() == 1) {
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            try {
                DataSourceAdapterDispatcher.getInstance().handle(applicationContext, dataSource.getClass()).registerLiquibase(dataSource, beanFactory);
            } catch (Throwable e) {
                log.error("register liquibase failed", e);
            }

        } else {
            dataSourceMap.forEach((k, v) -> {
                try {
                    DataSourceAdapterDispatcher.getInstance().handle(applicationContext, v.getClass()).registerLiquibase(v, beanFactory);
                } catch (Throwable e) {
                    log.error("register liquibase failed", e);
                }
            });
        }
        // 使注册的Liquibase生效
        BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, SpringLiquibase.class);
    }

}
