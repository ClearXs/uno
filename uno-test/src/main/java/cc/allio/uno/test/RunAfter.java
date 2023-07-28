package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 单元测试执行完成之后触发
 *
 * @author jiangwei
 * @date 2022/9/19 16:00
 * @since 1.1.0
 * @deprecated 自1.1.4版本后废弃，使用{@link org.junit.jupiter.api.AfterEach}代理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Deprecated
public @interface RunAfter {
}
