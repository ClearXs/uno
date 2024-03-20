package cc.allio.uno.test.runner;

import cc.allio.uno.core.reflect.Instantiation;
import cc.allio.uno.core.reflect.InstantiationBuilder;
import cc.allio.uno.core.reflect.InstantiationFeature;
import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.test.env.Environment;
import cc.allio.uno.test.env.annotation.AnnoConfigure;
import cc.allio.uno.test.env.annotation.Env;
import cc.allio.uno.test.env.annotation.EnvConfigure;
import cc.allio.uno.test.env.annotation.Extractor;
import cc.allio.uno.test.CoreTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 基于注解信息，构建环境配置信息。
 * <p>解析TestClass与指定的配置类上存在的注解，这些注解包含：</p>
 * <ul>
 *     <li>{@link Env}</li>
 *     <li>{@link Extractor}</li>
 * </ul>
 *
 * @author j.x
 * @date 2023/3/2 17:34
 * @see AnnoConfigure
 * @since 1.1.4
 */
@Slf4j
public class AnnoMetadataRunner implements RegisterRunner {

    @Override
    public void onRegister(CoreTest coreTest) throws Throwable {
        MergedAnnotations mergedAnnotations = coreTest.getAllAnnotations();
        for (MergedAnnotation<Annotation> anno : mergedAnnotations) {
            // 解析当前注解上的Env注解
            if (AnnotatedElementUtils.hasAnnotation(anno.getType(), Env.class)) {
                Env env = AnnotatedElementUtils.findMergedAnnotation(anno.getType(), Env.class);
                if (env != null) {
                    resolveEnv(coreTest, env);
                }
            }
        }
        for (MergedAnnotation<Annotation> anno : mergedAnnotations) {
            // 解析Extractor
            if (AnnotatedElementUtils.hasAnnotation(anno.getType(), AnnoConfigure.class)
                    && AnnotatedElementUtils.hasAnnotation(anno.getType(), Extractor.class)) {
                Extractor extractor = AnnotatedElementUtils.findMergedAnnotation(anno.getType(), Extractor.class);
                if (extractor != null) {
                    extractEnv(anno, extractor, coreTest);
                }
            }
        }
    }

    /**
     * 构建Environment实例，解析
     *
     * @param env Environment
     */
    public void resolveEnv(CoreTest coreTest, Env env) {
        Class<? extends Environment>[] envClasses = env.value();
        Instantiation<Environment> instantiation =
                InstantiationBuilder.<Environment>builder()
                        .addMultiForInstanceClasses(envClasses)
                        .setExcludeNull(true)
                        .build();
        instantiation.addFeature(InstantiationFeature.sort());
        instantiation.addFeature(InstantiationFeature.deduplicate());
        List<Environment> environments = instantiation.create();
        for (Environment environment : environments) {
            try {
                environment.support(coreTest);
            } catch (Throwable ex) {
                log.error("support environment {} happened error", environment.getClass().getName(), ex);
            }
        }
    }

    /**
     * 提取*Env的注解所包含的其他环境信息
     *
     * @param anno      *Env注解
     * @param extractor extractor
     * @param coreTest  coreTest
     */
    private void extractEnv(MergedAnnotation<Annotation> anno, Extractor extractor, CoreTest coreTest) {
        Class<? extends EnvConfigure> extractorClazz = extractor.value();
        EnvConfigure configureExtractor = ClassUtils.newInstanceIfErrorDefault(extractorClazz, null, null);
        if (configureExtractor != null) {
            // 动态生成bean 配置类
            BeanDefinition beanDefinition = configureExtractor.extract(coreTest, anno);
            GenericApplicationContext context = coreTest.getContext();
            if (beanDefinition != null && context != null) {
                context.registerBeanDefinition(anno.getType().getName(), beanDefinition);
            }
        }
    }

}
