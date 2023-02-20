package cc.allio.uno.starter.liquibase;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.auto.service.AutoService;

import javax.sql.DataSource;
import java.util.function.Predicate;

/**
 * Druid数据源适配器
 *
 * @author jiangwei
 * @date 2022/1/19 18:06
 * @since 1.0
 */
@AutoService(LiquibaseDataSourceAdapter.class)
public class DruidDataSourceAdapter extends BaseLiquibaseDataSourceAdapter {

	@Override
	public String dbType(DataSource dataSource) {
		DruidDataSource druidDataSource = (DruidDataSource) dataSource;
		return druidDataSource.getDbType();
	}

	@Override
	public Predicate<Class<? extends DataSource>> isAdapter() {
		return DruidDataSource.class::isAssignableFrom;
	}
}
