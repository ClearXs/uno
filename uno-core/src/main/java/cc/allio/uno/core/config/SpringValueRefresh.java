package cc.allio.uno.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link ValueRefresh}
 *
 * @author jiangw
 * @date 2021/1/8 11:21
 * @since 1.0
 */
@Slf4j
public class SpringValueRefresh implements ValueRefresh {

    private final ConfigurableListableBeanFactory beanFactory;

    private final ApplicationContext applicationContext;

    private static volatile SpringValueRefresh valueRefresh;

    private SpringValueRefresh(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "SpringValueRefresh requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;

    }

    /**
     * 1.解析@Value
     * 2.获取value（Property获取）
     * 3.改变值
     */
    @Override
    public synchronized void refresh(Object bean, String beanName) {
        List<FieldValueElement> elements = buildValueElements(bean, beanName);
        if (!CollectionUtils.isEmpty(elements)) {
            for (FieldValueElement element : elements) {
                try {
                    element.inject();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void refreshAll() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            refresh(bean, beanName);
        }
    }

    List<FieldValueElement> buildValueElements(Object bean, String beanName) {
        Class<?> clazz = bean.getClass();
        List<FieldValueElement> elements = new ArrayList<>();
        do {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Value value = declaredField.getAnnotation(Value.class);
                if (value != null) {
                    elements.add(new FieldValueElement(bean, beanName, declaredField));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null && clazz != Object.class);
        return elements;
    }

    class FieldValueElement {

        private final Object bean;

        private final Field field;

        private final String beanName;

        FieldValueElement(Object bean, String beanName, Field field) {
            this.bean = bean;
            this.beanName = beanName;
            this.field = field;
        }

        public void inject() throws IllegalAccessException {
            Object value;
            DependencyDescriptor desc = new DependencyDescriptor(this.field, true);
            Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
            TypeConverter typeConverter = beanFactory.getTypeConverter();
            try {
                value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
            } catch (BeansException | IllegalArgumentException ex) {
                log.info("@Value parse error: {}", ex.getMessage());
                value = "";
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                this.field.set(bean, value);
            }
        }
    }

    public static SpringValueRefresh getInstance(ApplicationContext applicationContext) {
        if (valueRefresh == null) {
            valueRefresh = new SpringValueRefresh(applicationContext);
        }
        return valueRefresh;
    }
}
