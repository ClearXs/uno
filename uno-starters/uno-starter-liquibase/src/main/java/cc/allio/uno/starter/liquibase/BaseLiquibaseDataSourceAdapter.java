package cc.allio.uno.starter.liquibase;

import cc.allio.uno.core.util.template.GenericTokenParser;
import cc.allio.uno.core.util.template.Tokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 提高是否有change-log文件的基本适配器
 *
 * @author jiangwei
 * @date 2022/1/19 20:34
 * @since 1.0
 */
@Slf4j
public abstract class BaseLiquibaseDataSourceAdapter implements LiquibaseDataSourceAdapter {

    private ApplicationContext applicationContext;

    private static final String XML = ".xml";

    private static final String YAML = ".yaml";

    private static final String JSON = ".json";

    private static final String SQL = ".sql";

    @Override
    public LiquibaseDataSourceAdapter setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        return this;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public String getChangeLog(DataSource dataSource) {
        String propertiesChangeLog = getPropertiesChangeLog(dataSource);
        if (!StringUtils.isEmpty(propertiesChangeLog)) {
            return propertiesChangeLog;
        }
        GenericTokenParser parser = getDbParser().apply(dataSource);
        String changeLogPath = changeLogPath(dataSource, parser);
        String changeLogName = changeLogName(dataSource, parser);
        return getMaybeChangeLog(changeLogPath, changeLogName);
    }

    /**
     * 获取可能的change-log文件路径
     *
     * @param changeLogPath         change-path
     * @param expectedChangeLogName 期望change-log名称，无后缀文件名称
     * @return 某个具体文件路径
     */
    protected String getMaybeChangeLog(String changeLogPath, String expectedChangeLogName) {
        String changeLog = changeLog(changeLogPath, expectedChangeLogName.concat(YAML));
        if (!StringUtils.isEmpty(changeLog)) {
            return changeLog;
        }
        changeLog = changeLog(changeLogPath, expectedChangeLogName.concat(XML));
        if (!StringUtils.isEmpty(changeLog)) {
            return changeLog;
        }
        changeLog = changeLog(changeLogPath, expectedChangeLogName.concat(JSON));
        if (!StringUtils.isEmpty(changeLog)) {
            return changeLog;
        }
        changeLog = changeLog(changeLogPath, expectedChangeLogName.concat(SQL));
        if (!StringUtils.isEmpty(changeLog)) {
            return changeLog;
        }
        return "";
    }

    /**
     * 获取change-log名称
     *
     * @param changeLogPath change-log文件存放的路径
     * @param changeLogName change-log文件具体名称
     * @return 可能能在目标目录找到的change-log文件
     */
    private String changeLog(String changeLogPath, String changeLogName) {
        Predicate<String> predicate = hasChangeLogFile(changeLogName);
        if (predicate.test(changeLogPath)) {
            Function<String, File> expect = getFile(changeLogName);
            File file = expect.apply(changeLogPath);
            if (Objects.nonNull(file)) {
                return changeLogPath.concat("/").concat(file.getName());
            }
        }
        return "";
    }

    /**
     * 获取change-path路径名称
     *
     * @param dataSource 数据源对象
     * @param parser     占位符解析器
     * @return change-log路径名称
     */
    protected String changeLogPath(DataSource dataSource, GenericTokenParser parser) {
        return parser.parse(CHANGE_LOG_PATH, content -> dbType(dataSource));
    }

    /**
     * 获取change-log文件名称
     *
     * @param dataSource 数据源对象
     * @param parser     占位符解析器
     * @return change-log文件名称
     */
    protected String changeLogName(DataSource dataSource, GenericTokenParser parser) {
        return parser.parse(CHANGE_LOG_NAME, content -> dbType(dataSource));
    }

    protected Function<DataSource, GenericTokenParser> getDbParser() {
        return dataSource -> new GenericTokenParser(Tokenizer.HASH_BRACE);
    }

    /**
     * 获取配置文件中的change-log路径，
     * <ul>
     *     <li>当spring.liquibase.changelog有值则取该路径的change-log</li>
     *     <li>否则取db/migrations/#{dbType}/db_migration.yaml</li>
     * </ul>
     *
     * @param dataSource 数据源对象
     * @return 可能存在的change-log路径
     */
    protected String getPropertiesChangeLog(DataSource dataSource) {
        return applicationContext.getEnvironment().getProperty("spring.liquibase.changelog");
    }
}
