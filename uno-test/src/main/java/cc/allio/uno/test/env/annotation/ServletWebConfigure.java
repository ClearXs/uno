package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.Annotation;

/**
 * {@link ServletWebEnv}注解抽取器
 *
 * @author j.x
 * @date 2023/3/9 20:08
 * @since 1.1.4
 */
public class ServletWebConfigure extends DynamicConfigure {
    @Override
    protected AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        AnnotationDescription.Builder annoBuilder = AnnotationDescription.Builder.ofType(EnableWebMvc.class);
        return new AnnotationDescription[]{annoBuilder.build()};
    }
}
