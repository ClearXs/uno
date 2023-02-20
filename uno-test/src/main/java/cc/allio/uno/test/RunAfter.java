package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 单元测试执行完成之后触发
 *
 * @author jiangwei
 * @date 2022/9/19 16:00
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RunAfter {
}
