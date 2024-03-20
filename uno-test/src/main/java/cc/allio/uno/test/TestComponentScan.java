package cc.allio.uno.test;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

/**
 * 测试组件扫描，操作自{@link ComponentScan}
 *
 * @author j.x
 * @date 2023/4/20 17:01
 * @see RunTestAttributes
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestComponentScan {

    /**
     * 扫描的组件
     */
    Class<?>[] value() default {};

    /**
     * 扫描的路径
     */
    String[] basePackages() default {};
}
