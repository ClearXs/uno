package cc.allio.uno.starter.liquibase;

import com.alibaba.druid.pool.DruidDataSource;
import cc.allio.uno.core.util.template.GenericTokenParser;
import cc.allio.uno.test.BaseTestCase;
import org.junit.jupiter.api.Test;

class DruidDataSourceAdapterTest extends BaseTestCase {

	DruidDataSource dataSource;

	DruidDataSourceAdapter adapter;

	@Override
	protected void onInit() throws Throwable {
		dataSource = new DruidDataSource();
		dataSource.setDbType("postgres");
		adapter = new DruidDataSourceAdapter();
	}

	@Test
	void testDbType() {
		String dbType = adapter.dbType(dataSource);
		assertEquals("postgres", dbType);
	}

	@Test
	void testChangeFilePath() {
		GenericTokenParser parser = adapter.getDbParser().apply(dataSource);
		String changeLogPath = adapter.changeLogPath(dataSource, parser);
		assertEquals("classpath:db/migrations/postgres", changeLogPath);
	}

	@Test
	void testChangeFileName() {
		GenericTokenParser parser = adapter.getDbParser().apply(dataSource);
		String changeLogName = adapter.changeLogName(dataSource, parser);
		assertEquals("db_migration", changeLogName);
	}

	@Test
	void testGetChangeLog() {
		assertEquals("classpath:db/migrations/postgres/db_migration.yaml", adapter.getChangeLog(dataSource));
	}

	@Override
	protected void onDown() throws Throwable {
	}
}
