package cc.allio.uno.test.env;

import cc.allio.uno.test.BaseCoreTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.annotation.Priority;

/**
 * 数据源测试环境类
 *
 * @author jiangwei
 * @date 2022/2/14 14:11
 * @since 1.0
 */
@Priority(Integer.MIN_VALUE)
public class DatasourceTestEnvironment implements TestSpringEnvironment {

    static final DataSourceProperties DEFAULT_DATA_SOURCE_PROPERTIES = new DataSourceProperties();
    final DataSourceProperties dataSourceProperties;

    public DatasourceTestEnvironment() {
        this(DEFAULT_DATA_SOURCE_PROPERTIES);
    }

    public DatasourceTestEnvironment(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public void support(BaseCoreTest test) {
        test.addProperty("spring.datasource.driver-class-name", test.getProperty("spring.datasource.driver-class-name", dataSourceProperties.getDriverClassName()));
        test.addProperty("spring.datasource.url", test.getProperty("spring.datasource.url", dataSourceProperties.getUrl()));
        test.addProperty("spring.datasource.username", test.getProperty("spring.datasource.username", dataSourceProperties.getUsername()));
        test.addProperty("spring.datasource.password", test.getProperty("spring.datasource.password", dataSourceProperties.getPassword()));
        test.registerComponent(DataSourceAutoConfiguration.class);
    }

}
