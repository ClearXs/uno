package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;

/**
 * {@link FeignEnv}配置抽取器
 *
 * @author jiangwei
 * @date 2023/3/9 12:03
 * @since 1.1.4
 */
public class FeignConfigure extends DynamicConfigure {
    @Override
    protected AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        AnnotationDescription.Builder annoBuilder = AnnotationDescription.Builder.ofType(EnableFeignClients.class);
        annoBuilder = annoBuilder.defineArray("value", annotation.getStringArray("value"))
                .defineArray("basePackages", annotation.getStringArray("basePackages"))
                .defineTypeArray("basePackageClasses", annotation.getClassArray("basePackageClasses"))
                .defineTypeArray("defaultConfiguration", annotation.getClassArray("defaultConfiguration"))
                .defineTypeArray("clients", annotation.getClassArray("clients"));
        return new AnnotationDescription[]{annoBuilder.build()};
    }
}
