package cc.allio.uno.test.env.annotation;

import cc.allio.uno.test.CoreTest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.MergedAnnotation;

import java.lang.annotation.Annotation;

/**
 * 基于注解配置的抽取器，动态生成Bean Definition。能够
 *
 * @author jiangwei
 * @date 2023/3/2 17:54
 * @since 1.1.4
 */
public interface EnvConfigure {

    /**
     * 抽取给定的注解元数据，并生成{@link BeanDefinition}
     *
     * @param coreTest
     * @param annotation 注解元数据
     * @return BeanDefinition
     */
    BeanDefinition extract(CoreTest coreTest, MergedAnnotation<Annotation> annotation);

}
