package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.env.annotation.properties.*;
import cc.allio.uno.test.env.FeignEnvironment;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

import java.lang.annotation.*;

/**
 * 混合注解，构建Feign运行时环境。注解内容替代{@link EnableFeignClients}
 * <p>对应的配置注解为
 * <ul>
 *     <li>{@link FeignClientProperties}</li>
 *     <li>{@link FeignEncoderProperties}</li>
 *     <li>{@link FeignHttpClientProperties}</li>
 *     <li>{@link RibbonEagerLoadProperties}</li>
 *     <li>{@link ServerIntrospectorProperties}</li>
 * </ul>
 * </p>
 *
 * @author jiangwei
 * @date 2023/3/9 11:57
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Extractor(FeignConfigure.class)
@Env({FeignEnvironment.class})
@ServletWebEnv
public @interface FeignEnv {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
     * declarations e.g.: {@code @ComponentScan("org.my.pkg")} instead of
     * {@code @ComponentScan(basePackages="org.my.pkg")}.
     *
     * @return the array of 'basePackages'.
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components.
     * <p>
     * {@link #value()} is an alias for (and mutually exclusive with) this attribute.
     * <p>
     * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
     * package names.
     *
     * @return the array of 'basePackages'.
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to
     * scan for annotated components. The package of each class specified will be scanned.
     * <p>
     * Consider creating a special no-op marker class or interface in each package that
     * serves no purpose other than being referenced by this attribute.
     *
     * @return the array of 'basePackageClasses'.
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * A custom <code>@Configuration</code> for all feign clients. Can contain override
     * <code>@Bean</code> definition for the pieces that make up the client, for instance
     * {@link feign.codec.Decoder}, {@link feign.codec.Encoder}, {@link feign.Contract}.
     *
     * @return list of default configurations
     * @see FeignClientsConfiguration for the defaults
     */
    Class<?>[] defaultConfiguration() default {};

    /**
     * List of classes annotated with @FeignClient. If not empty, disables classpath
     * scanning.
     *
     * @return list of FeignClient classes
     */
    Class<?>[] clients() default {};
}
