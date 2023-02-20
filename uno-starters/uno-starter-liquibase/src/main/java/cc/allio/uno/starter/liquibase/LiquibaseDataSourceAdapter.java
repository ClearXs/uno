package cc.allio.uno.starter.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Liquibase适配器，对于不同的数据源类型来创建Liquibase对象
 *
 * @author jiangwei
 * @date 2022/1/19 16:28
 * @since 1.0
 */
public interface LiquibaseDataSourceAdapter {

	/**
	 * change-log配置文件的目录
	 */
	String CHANGE_LOG_PATH = "classpath:db/migrations/#{dbType}";

	/**
	 * change-log文件名称
	 */
	String CHANGE_LOG_NAME = "db_migration";

	/**
	 * 获取change-log文件路径。</br>
	 * 默认优先级顺序：db_migration_#{dbType}（或者db_migration_#{dbType}-#{dynamic}）>.yaml>.yml>.xml
	 *
	 * @param dataSource 数据源对象
	 * @return change-log文件目录
	 * @see BaseLiquibaseDataSourceAdapter
	 */
	String getChangeLog(DataSource dataSource);

	/**
	 * 获取dbType
	 *
	 * @param dataSource 数据源对象
	 * @return dbType字符串
	 */
	String dbType(DataSource dataSource);

	/**
	 * 测试当前数据源适配器是否可以适配
	 *
	 * @return 返回一个断言对象
	 */
	Predicate<Class<? extends DataSource>> isAdapter();

	/**
	 * 设置application实例
	 *
	 * @param applicationContext 上下文实例
	 * @return 当前实例
	 */
	LiquibaseDataSourceAdapter setApplicationContext(ApplicationContext applicationContext);

	/**
	 * 获取ApplicationContext上下文
	 *
	 * @return application上下文实例
	 */
	ApplicationContext getApplicationContext();

	// ---------------- default ----------------

	/**
	 * 获取指定目录下的所以文件
	 *
	 * @return 入参为文件路径，获取文件List
	 */
	default Function<String, List<File>> getFiles() {
		return path -> {
			try {
				File changeFile = ResourceUtils.getFile(path);
				// 判断边界条件
				File[] listableFile = changeFile.listFiles();
				if (ObjectUtils.isEmpty(listableFile)) {
					return Collections.emptyList();
				}
				return Arrays.stream(listableFile)
					.filter(File::isFile)
					.collect(Collectors.toList());
			} catch (FileNotFoundException e) {
				return Collections.emptyList();
			}
		};
	}

	/**
	 * 根据文件目录判断目标路径存在有change-log文件：</br>
	 * 1.判断是否有目录</br>
	 * 2.如果有目录，判断目录下是否有以db_migration.yaml或者db_migration.xml或者db_migration.json为文件名</br>
	 *
	 * @param maybeName change-log文件名称或者.yaml文件
	 * @return 返回一个断言对象
	 * @see BaseLiquibaseDataSourceAdapter
	 * @see DynamicRoutingDataSourceAdapter
	 */
	default Predicate<String> hasChangeLogFile(String maybeName) {
		return path ->
			getFiles().apply(path)
				.stream()
				.anyMatch(file -> file.getName().equals(maybeName));
	}

	/**
	 * 获取文件，调用{@link String#contains(CharSequence)}方法进行判断。
	 *
	 * @param expectedFileName 期望获取的文件名称，可能不是具体文件名称。
	 * @return 以文件路径作为入参，获取具体文件（当找不到时返回null）
	 */
	default Function<String, File> getFile(String expectedFileName) {
		return path -> getFiles().apply(path)
				.stream()
				.filter(file -> file.getName().equals(expectedFileName))
				.findFirst()
				.orElse(null);
	}

	/**
	 * 向Spring注册Liquibase对象
	 *
	 * @param dataSource  数据源对象
	 * @param beanFactory bean工厂
	 */
	default void registerLiquibase(DataSource dataSource, DefaultListableBeanFactory beanFactory) {
		String changeLog = getChangeLog(dataSource);
		if (StringUtils.isEmpty(changeLog)) {
			return;
		}
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setChangeLog(changeLog);
		liquibase.setDataSource(dataSource);
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpringLiquibase.class, () -> liquibase);
		beanFactory.registerBeanDefinition("liquibase-".concat(dbType(dataSource)), beanDefinitionBuilder.getRawBeanDefinition());
	}
}
