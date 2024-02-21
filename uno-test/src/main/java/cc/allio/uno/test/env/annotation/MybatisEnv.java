package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.annotation.properties.DataSourceProperties;
import cc.allio.uno.test.env.annotation.properties.MybatisProperties;
import cc.allio.uno.test.env.DataSourceEnvironment;
import cc.allio.uno.test.env.MybatisEnvironment;
import cc.allio.uno.test.env.TransactionEnvironment;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 混合注解，构建Mybatis的环境。代替{@link MapperScan}。
 * <p>
 * 对应的注解配置；
 * <ul>
 *     <li>{@link MybatisProperties}</li>
 *     <li>{@link DataSourceProperties}</li>
 * </ul>
 * </p>
 *
 * @author jiangwei
 * @date 2023/3/2 17:14
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Extractor(MybatisConfigure.class)
@Env({MybatisEnvironment.class, DataSourceEnvironment.class, TransactionEnvironment.class})
public @interface MybatisEnv {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @MapperScan("org.my.pkg")} instead of {@code @MapperScan(basePackages = "org.my.pkg"})}.
     *
     * @return base package names
     */
    @AliasFor("basePackages")
    String[] value() default {};

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces with at least one method will be
     * registered; concrete classes will be ignored.
     *
     * @return base package names for scanning mapper interface
     */
    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
     * package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that serves no purpose other than being
     * referenced by this attribute.
     *
     * @return classes that indicate base package for scanning mapper interface
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components within the Spring container.
     *
     * @return the class of {@link BeanNameGenerator}
     */
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     *
     * @return the annotation that the scanner will search for
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have the specified interface class as a
     * parent.
     * <p>
     * Note this can be combined with annotationClass.
     *
     * @return the parent that the scanner will search for
     */
    Class<?> markerInterface() default Class.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionTemplate}
     */
    String sqlSessionTemplateRef() default "";

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is more than one in the spring context.
     * Usually this is only needed when you have more than one datasource.
     *
     * @return the bean name of {@code SqlSessionFactory}
     */
    String sqlSessionFactoryRef() default "";

    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     * @return the class of {@code MapperFactoryBean}
     */
    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;

    /**
     * Whether enable lazy initialization of mapper bean.
     *
     * <p>
     * Default is {@code false}.
     * </p>
     *
     * @return setValue {@code true} to enable lazy initialization
     * @since 2.0.2
     */
    String lazyInitialization() default "";

    /**
     * Specifies the default scope of scanned mappers.
     *
     * <p>
     * Default is {@code ""} (equiv to singleton).
     * </p>
     *
     * @return the default scope
     */
    String defaultScope() default AbstractBeanDefinition.SCOPE_DEFAULT;

}
