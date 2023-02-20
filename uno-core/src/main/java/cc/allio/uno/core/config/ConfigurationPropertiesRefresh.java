package cc.allio.uno.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * configuration properties refresh {@link ConfigurationProperties}
 *
 * @author jiangw
 * @date 2021/1/9 14:50
 * @since 1.0
 */
@Slf4j
public class ConfigurationPropertiesRefresh implements ValueRefresh {

    private Binder binder;

    private ConfigurationBeanFactoryMetadata beanFactoryMetadata;

    private static ConfigurationPropertiesRefresh refresh;

    private final ApplicationContext context;

    private ConfigurationPropertiesRefresh(ApplicationContext applicationContext) {
        this.context = applicationContext;
        try {
            Environment environment = applicationContext.getEnvironment();
            if (environment instanceof ConfigurableEnvironment) {
                ConfigurableEnvironment configurableEnvironment = (ConfigurableEnvironment) environment;
                MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
                this.binder = new Binder(ConfigurationPropertySources.from(propertySources),
                        new PropertySourcesPlaceholdersResolver(propertySources),
                        new DefaultConversionService(),
                        ((ConfigurableApplicationContext) applicationContext).getBeanFactory()::copyRegisteredEditorsTo);
            }

            this.beanFactoryMetadata = applicationContext.getBean(ConfigurationBeanFactoryMetadata.class);
        } catch (NoSuchBeanDefinitionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void refresh(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        ConfigurationProperties annotation = clazz.getAnnotation(ConfigurationProperties.class);
        if (annotation != null && binder != null) {
            ResolvableType type = getBeanType(bean, beanName);
            Validated validated = getAnnotation(bean, beanName, Validated.class);
            Annotation[] annotations = (validated != null) ? new Annotation[]{annotation, validated}
                    : new Annotation[]{annotation};
            Bindable<?> target = Bindable.of(type).withExistingValue(bean).withAnnotations(annotations);
            this.binder.bind(annotation.prefix(), target);
        }
    }

    @Override
    public void refreshAll() {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = context.getBean(beanName);
            refresh(bean, beanName);
        }
    }

    private ResolvableType getBeanType(Object bean, String beanName) {
        Method factoryMethod = this.beanFactoryMetadata.findFactoryMethod(beanName);
        if (factoryMethod != null) {
            return ResolvableType.forMethodReturnType(factoryMethod);
        }
        return ResolvableType.forClass(bean.getClass());
    }

    private <A extends Annotation> A getAnnotation(Object bean, String beanName, Class<A> type) {
        A annotation = this.beanFactoryMetadata.findFactoryAnnotation(beanName, type);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(bean.getClass(), type);
        }
        return annotation;
    }

    public static ConfigurationPropertiesRefresh getInstance(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new NullPointerException("application context is null");
        }
        if (refresh == null) {
            refresh = new ConfigurationPropertiesRefresh(applicationContext);
        }
        return refresh;
    }
}
