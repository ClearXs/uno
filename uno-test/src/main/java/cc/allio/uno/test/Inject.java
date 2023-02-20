package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 单元测试字段注册标识
 *
 * @author jiangwei
 * @date 2022/9/19 15:11
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
}
