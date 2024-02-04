package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import net.bytebuddy.description.annotation.AnnotationDescription;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;

/**
 * {@link MybatisEnv}注解抽取器
 *
 * @author jiangwei
 * @date 2023/3/2 18:05
 * @since 1.1.4
 */
public class MybatisConfigure extends DynamicConfigure {

    @Override
    protected AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        AnnotationDescription.Builder annoBuilder = AnnotationDescription.Builder.ofType(MapperScan.class);
        String coreTestPackageName = coreTest.getTestClass().getPackage().getName();
        String[] values = annotation.getStringArray("value");
        String[] basePackages = annotation.getStringArray("basePackages");
        // values and basePackages mutual alias for
        if (values.length == 0 && basePackages.length == 0) {
            // if not specified basePackages, use test package path and addition .mapper
            basePackages = new String[]{coreTestPackageName.concat(".mapper")};
        }
        annoBuilder = annoBuilder.defineArray("value", values)
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
