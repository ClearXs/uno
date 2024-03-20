package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * {@link ImportAutoConfiguration}的属性描述
 *
 * @author j.x
 * @date 2023/7/5 14:35
 * @since 1.1.4
 */
public class ImportAutoConfigure extends DynamicConfigure {

    @Override
    protected AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        Set<Class<?>> componentsClasses = coreTest.getRunTestAttributes().getAutoConfigurationClasses();
        AnnotationDescription.Builder annoBuilder = AnnotationDescription.Builder.ofType(org.springframework.boot.autoconfigure.ImportAutoConfiguration.class);
        annoBuilder = annoBuilder.defineTypeArray("classes", componentsClasses.toArray(new Class[0]));
        return new AnnotationDescription[]{annoBuilder.build()};
    }
}
