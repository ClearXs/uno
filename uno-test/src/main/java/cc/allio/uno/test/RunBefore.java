package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 单元测试执行之前触发该注解标识的方法
 *
 * @author jiangwei
 * @date 2022/9/19 16:00
 * @since 1.1.0
 * @deprecated 自1.1.4版本后废弃，使用{@link org.junit.jupiter.api.BeforeEach}代替
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Deprecated
public @interface RunBefore {
}
