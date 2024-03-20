package cc.allio.uno.test.env.annotation;

import java.lang.annotation.*;

/**
 * 描述{@link org.springframework.boot.autoconfigure.ImportAutoConfiguration}。当测试类加上该配置后，会自动的转换为{@link org.springframework.boot.autoconfigure.ImportAutoConfiguration#classes()}的数值
 *
 * @author j.x
 * @date 2023/7/5 14:33
 * @since 1.1.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AnnoConfigure
@Extractor(ImportAutoConfigure.class)
public @interface ImportAutoConfiguration {

}
