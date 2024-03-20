package cc.allio.uno.test.env.annotation.properties;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import org.apache.ibatis.io.DefaultVFS;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

/**
 * {@link com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties}注解描述
 *
 * @author j.x
 * @date 2023/3/6 16:52
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Properties("mybatis-plus")
public @interface MybatisPlusProperties {

    /**
     * Location of MyBatis xml config file.
     */
    String configLocation() default "";

    /**
     * Locations of MyBatis mapper files.
     */
    String[] mapperLocations() default {"classpath*:/mapper/**/*.xml"};

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
     * Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
     */
    ExecutorType executorType() default ExecutorType.SIMPLE;

    /**
     * TODO 枚举包扫描
     */
    String typeEnumsPackage() default "";

    /**
     * A Configuration object for customize default settings. If {@link #configLocation}
     * is specified, this property is not used.
     */
    MybatisConfiguration mybatisConfiguration();

    /**
     * {@link com.baomidou.mybatisplus.core.MybatisConfiguration}的注解描述
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface MybatisConfiguration {
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

    /**
     * {@link  com.baomidou.mybatisplus.core.config.GlobalConfig}的注解描述
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface GlobalConfig {

        /**
         * 是否开启 LOGO
         */
        boolean banner() default true;

        /**
         * 是否初始化 SqlRunner
         */
        boolean enableSqlRunner() default false;

        /**
         * Mapper父类
         */
        Class<?> superMapperClass() default Mapper.class;

        /**
         * 数据库相关配
         */
        DbConfig dbConfig();
    }

    /**
     * {@link cn.easyes.core.config.GlobalConfig.DbConfig}的注解描述
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Properties
    @interface DbConfig {

        /**
         * 主键类型
         */
        IdType idType() default IdType.ASSIGN_ID;

        /**
         * 表名前缀
         */
        String tablePrefix() default "";

        /**
         * schema
         */
        String schema() default "";

        /**
         * db字段 format
         * <p>
         * 例: `%s`
         * <p>
         * 对主键无效
         */
        String columnFormat() default "";

        /**
         * entity 的字段(property)的 format,只有在 column as property 这种情况下生效
         * <p>
         * 例: `%s`
         * <p>
         * 对主键无效
         */
        String propertyFormat() default "";

        /**
         * 实验性功能,占位符替换,等同于 {@link com.baomidou.mybatisplus.extension.plugins.inner.ReplacePlaceholderInnerInterceptor},
         * 只是这个属于启动时替换,用得地方多会启动慢一点点,不适用于其他的 {@link org.apache.ibatis.scripting.LanguageDriver}
         */
        boolean replacePlaceholder() default false;

        /**
         * 转义符
         * <p>
         * 配合 {@link #replacePlaceholder} 使用时有效
         * <p>
         * 例: " 或 ' 或 `
         */
        String escapeSymbol() default "";

        /**
         * 表名是否使用驼峰转下划线命名,只对表名生效
         */
        boolean tableUnderline() default true;

        /**
         * 大写命名,对表名和字段名均生效
         */
        boolean capitalMode() default false;

        /**
         * 逻辑删除全局属性名
         */
        String logicDeleteField() default "";

        /**
         * 逻辑删除全局值（默认 1、表示已删除）
         */
        String logicDeleteValue() default "1";

        /**
         * 逻辑未删除全局值（默认 0、表示未删除）
         */
        String logicNotDeleteValue() default "0";

        /**
         * 字段验证策略之 insert
         */
        FieldStrategy insertStrategy() default FieldStrategy.NOT_NULL;

        /**
         * 字段验证策略之 update
         */
        FieldStrategy updateStrategy() default FieldStrategy.NOT_NULL;

        /**
         * 字段验证策略之 select
         */
        FieldStrategy selectStrategy() default FieldStrategy.NOT_NULL;
    }

}
