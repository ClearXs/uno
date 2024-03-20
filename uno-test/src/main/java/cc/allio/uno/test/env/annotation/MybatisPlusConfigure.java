package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;

/**
 * {@link MybatisPlusEnv}注解抽取器
 *
 * @author j.x
 * @date 2023/3/6 16:26
 * @since 1.1.4
 */
public class MybatisPlusConfigure extends DynamicConfigure {

    @Override
    protected AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        AnnotationDescription.Builder annoBuilder = AnnotationDescription.Builder.ofType(MapperScan.class);
        String[] basePackages = annotation.getStringArray("basePackages");
        if (basePackages.length == 0) {
            String packageName = coreTest.getTestClass().getPackage().getName();
            basePackages = new String[]{packageName};
        }
        annoBuilder = annoBuilder.defineArray("value", annotation.getStringArray("value"))
                .defineArray("basePackages", basePackages)
                .defineTypeArray("basePackageClasses", annotation.getClassArray("basePackageClasses"))
                .define("nameGenerator", annotation.getClass("nameGenerator"))
                .define("annotationClass", annotation.getClass("annotationClass"))
                .define("markerInterface", annotation.getClass("markerInterface"))
                .define("sqlSessionTemplateRef", annotation.getString("sqlSessionTemplateRef"))
                .define("sqlSessionFactoryRef", annotation.getString("sqlSessionFactoryRef"))
                .define("factoryBean", annotation.getClass("factoryBean"))
                .define("lazyInitialization", annotation.getString("lazyInitialization"))
                .define("defaultScope", annotation.getString("defaultScope"));
        return new AnnotationDescription[]{annoBuilder.build()};
    }
}