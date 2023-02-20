package cc.allio.uno.starter.liquibase;

import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 数据源适配处理器，数据源分派
 *
 * @author jiangwei
 * @date 2022/1/19 18:04
 * @since 1.0
 */
public class DataSourceAdapterDispatcher {

	/**
	 * 适配器缓存
	 */
	private final List<LiquibaseDataSourceAdapter> adapters = new ArrayList<>();

	/**
	 * 单例
	 */
	private static final DataSourceAdapterDispatcher DISPATCHER = new DataSourceAdapterDispatcher();

	private DataSourceAdapterDispatcher() {
		ServiceLoader<LiquibaseDataSourceAdapter> load = ServiceLoader.load(LiquibaseDataSourceAdapter.class);
		for (LiquibaseDataSourceAdapter liquibaseDataSourceAdapter : load) {
			adapters.add(liquibaseDataSourceAdapter);
		}
	}

	/**
	 * 数据源适配派发
	 *
	 * @param clazz 数据源Class对象
	 * @throws NullPointerException 当没有找到合适的适配器时抛出
	 */
	public LiquibaseDataSourceAdapter handle(ApplicationContext applicationContext, Class<? extends DataSource> clazz) {
		return adapters.stream()
			.filter(adapter -> adapter.isAdapter().test(clazz))
			.findFirst()
			.orElseThrow(NullPointerException::new)
			.setApplicationContext(applicationContext);
	}

	public static DataSourceAdapterDispatcher getInstance() {
		return DISPATCHER;
	}

}
