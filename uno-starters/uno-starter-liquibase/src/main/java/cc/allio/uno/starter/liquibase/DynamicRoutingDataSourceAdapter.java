package cc.allio.uno.starter.liquibase;

import cc.allio.uno.core.util.template.GenericTokenParser;
import cc.allio.uno.core.util.template.Tokenizer;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 多数据源适配器
 *
 * @author jiangwei
 * @date 2022/1/19 21:10
 * @since 1.0
 */
@Slf4j
@AutoService(LiquibaseDataSourceAdapter.class)
public class DynamicRoutingDataSourceAdapter extends BaseLiquibaseDataSourceAdapter {

    private static final String CHANGE_LOG_PROPERTIES = "spring.datasource.dynamic.datasource.#{dynamic}.liquibase.changelog";

    @Override
    public String dbType(DataSource dataSource) {
        ItemDataSource itemDataSource = (ItemDataSource) dataSource;
        DataSource realDataSource = itemDataSource.getRealDataSource();
        return DataSourceAdapterDispatcher.getInstance().handle(null, realDataSource.getClass()).dbType(realDataSource);
    }

    /**
     * @param dataSource 数据源对象
     * @param parser     占位符解析器
     * @return 获取以多数据源的db_migration-#{dynamicType}名称的文件名称
     */
    @Override
    protected String changeLogName(DataSource dataSource, GenericTokenParser parser) {
        String changeLogName = super.changeLogName(dataSource, parser);
        ItemDataSource itemDataSource = (ItemDataSource) dataSource;
        return changeLogName.concat("-").concat(itemDataSource.getName());
    }

    @Override
    protected String getPropertiesChangeLog(DataSource dataSource) {
        ApplicationContext applicationContext = getApplicationContext();
        GenericTokenParser parser = new GenericTokenParser(Tokenizer.HASH_BRACE);
        String changeLogProperties = parser.parse(CHANGE_LOG_PROPERTIES, content -> ((ItemDataSource) dataSource).getName());
        return applicationContext.getEnvironment().getProperty(changeLogProperties);
    }

    @Override
    public void registerLiquibase(DataSource dataSource, DefaultListableBeanFactory beanFactory) {
        DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> currentDataSources = dynamicRoutingDataSource.getCurrentDataSources();
        currentDataSources.forEach((k, v) -> super.registerLiquibase(v, beanFactory));
    }

    @Override
    public Predicate<Class<? extends DataSource>> isAdapter() {
        return clazz -> {
            boolean present = ClassUtils.isPresent("com.baomidou.dynamic.datasource.DynamicRoutingDataSource", ClassUtils.getDefaultClassLoader());
            if (present) {
                return DynamicRoutingDataSource.class.isAssignableFrom(clazz);
            }
            return false;
        };
    }
}
