package cc.allio.uno.test.env.annotation.properties;

import org.apache.ibatis.io.DefaultVFS;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

/**
 * {@link org.mybatis.spring.boot.autoconfigure.MybatisProperties}注解描述
 *
 * @author jiangwei
 * @date 2023/3/3 12:13
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("mybatis")
public @interface MybatisProperties {

    /**
     * Location of MyBatis xml config file.
     */
    String configLocation() default "";

    /**
     * Locations of MyBatis mapper files.
     */
    String[] mapperLocations() default {};

    /**
     * Packages to search type aliases. (Package delimiters are ",; \t\n")
     */
    String typeAliasesPackage() default "";

    /**
     * Packages to search for type handlers. (Package delimiters are ",; \t\n")
     */
    String typeHandlersPackage() default "";

    /**
     * Indicates whether perform presence check of the MyBatis xml config file.
     */
    boolean checkConfigLocation() default false;

    /**
     * Execution mode for org.mybatis.spring.SqlSessionTemplate.
     */
    ExecutorType executorType() default ExecutorType.SIMPLE;

    /**
     * The default scripting language driver class. (Available when use together with mybatis-spring 2.0.2+)
     */
    Class<? extends LanguageDriver> defaultScriptingLanguageDriver() default XMLLanguageDriver.class;

    /**
     * A Configuration object for customize default settings. If {@link #configLocation} is specified, this property is
     * not used.
     */
    Configuration configuration();

    /**
     * {@link org.apache.ibatis.session.Configuration}注解描述
     *
     * @author jiangwei
     * @date 2023/3/9 15:14
     * @since 1.1.4
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties("mybatis")
    @interface Configuration {
        boolean safeRowBoundsEnabled() default false;

        boolean safeResultHandlerEnabled() default true;

        boolean mapUnderscoreToCamelCase() default false;

        boolean aggressiveLazyLoading() default false;

        boolean multipleResultSetsEnabled() default true;

        boolean useGeneratedKeys() default false;

        boolean useColumnLabel() default true;

        boolean cacheEnabled() default true;

        boolean callSettersOnNulls() default false;

        boolean useActualParamName() default true;

        boolean returnInstanceForEmptyRow() default false;

        boolean shrinkWhitespacesInSql() default false;

        boolean nullableOnForEach() default false;

        String logPrefix() default "";

        Class<? extends Log> logImpl() default Log4j2Impl.class;

        Class<? extends VFS> vfsImpl() default DefaultVFS.class;

        LocalCacheScope localCacheScope() default LocalCacheScope.SESSION;

        JdbcType jdbcTypeForNull() default JdbcType.OTHER;

        String[] lazyLoadTriggerMethods() default {"equals", "clone", "hashCode", "toString"};

        int defaultStatementTimeout() default 0;

        int defaultFetchSize() default 0;

        ResultSetType defaultResultSetType() default ResultSetType.DEFAULT;

        ExecutorType defaultExecutorType() default ExecutorType.SIMPLE;

        AutoMappingBehavior autoMappingBehavior() default AutoMappingBehavior.PARTIAL;

        AutoMappingUnknownColumnBehavior autoMappingUnknownColumnBehavior() default AutoMappingUnknownColumnBehavior.NONE;

        boolean lazyLoadingEnabled() default false;

        String databaseId() default "";
    }
}
