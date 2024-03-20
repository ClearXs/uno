package cc.allio.uno.test;

import java.lang.annotation.*;

/**
 * 字段注入，除此之外还还可以使用{@link jakarta.annotation.Resource}、{@link org.springframework.beans.factory.annotation.Autowired}
 *
 * @author j.x
 * @date 2022/9/19 15:11
 * @since 1.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
}
