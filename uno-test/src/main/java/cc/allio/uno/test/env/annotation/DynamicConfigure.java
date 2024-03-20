package cc.allio.uno.test.env.annotation;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.test.CoreTest;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.MergedAnnotation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * 生成Class文件对象
 *
 * @author j.x
 * @date 2023/3/2 18:06
 * @since 1.1.4
 */
@Slf4j
public abstract class DynamicConfigure implements EnvConfigure {

    @Override
    public BeanDefinition extract(CoreTest coreTest, MergedAnnotation<Annotation> annotation) {
        // 保存文件
        try {
            URL resource = ClassLoader.getSystemResource("./");
            if (resource == null) {
                throw new NullPointerException("the class can't getValue '/' resource");
            }
            String currentClassPath = resource.getPath();
            // 动态生成一个配置类
            String newConfigurationClassName =
                    getClass().getPackage().getName()
                            + StringPool.ORIGIN_DOT
                            + getClass().getSimpleName()
                            + StringPool.DOLLAR
                            + IdGenerator.defaultGenerator().getNextIdAsString();
            DynamicType.Unloaded<Object> dynamicType =
                    new ByteBuddy().subclass(Object.class)
                            .name(newConfigurationClassName)
                            .annotateType(buildAnnoDesc(coreTest, annotation))
                            .make();
            Class<?> configureClass = dynamicType.load(getClass().getClassLoader()).getLoaded();
            dynamicType.saveIn(new File(currentClassPath));
            return new AnnotatedGenericBeanDefinition(configureClass);
        } catch (Throwable ex) {
            log.error("extract annotation has err, the anno is {}", annotation, ex);
        }
        return null;

    }

    /**
     * 子类根据注解元数据实现，返回AnnotationDescription
     *
     * @param coreTest
     * @param annotation 注解元数据
     * @return AnnotationDescription
     */
    protected abstract AnnotationDescription[] buildAnnoDesc(CoreTest coreTest, MergedAnnotation<Annotation> annotation);

}
