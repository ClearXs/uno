package cc.allio.uno.core.annotation;

import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;

/**
 * 从给定的<code>annotationClasses</code>获取{@link Annotated}实体
 *
 * @author jiangwei
 * @date 2022/12/9 15:34
 * @since 1.1.2
 */
public class BeanAnnotated implements AnnotatedCreator {

    private final Collection<Class<?>> annotationClasses;

    public BeanAnnotated(Collection<Class<?>> annotationClasses) {
        this.annotationClasses = annotationClasses;
    }

    @Override
    public Annotated oneOrNull(ListMultimap<Class<? extends Annotation>, Annotated> index, Class<? extends Annotation> annotationClass) throws DocumentFactoryException {
        List<Annotated> annotated = index.get(annotationClass);
        if (annotated.isEmpty()) {
            return null;
        } else if (annotated.size() > 1) {
            String descriptions = annotated.stream().map(Annotated::getDescription).collect(joining(", "));
            throw new DocumentFactoryException("Annotation @" + annotationClass.getSimpleName() + " is present multiple times: " + descriptions);
        } else {
            return annotated.get(0);
        }
    }

    @Override
    public ListMultimap<Class<? extends Annotation>, Annotated> index(Class<?> clazz) throws DocumentFactoryException {
        ListMultimap<Class<? extends Annotation>, Annotated> index = MultimapBuilder.hashKeys().arrayListValues().build();
        annotatedFrom(clazz)
                .forEach(annotated -> annotated.getAnnotations()
                        .forEach(t -> index.put(t.annotationType(), annotated)));
        return index;
    }

    @Override
    public List<Annotated> annotatedFrom(Class<?> clazz) throws DocumentFactoryException {
        List<Annotated> annotated = new ArrayList<>();
        Class<?> repeatClazz = clazz;
        while (repeatClazz != null) {
            for (Field field : repeatClazz.getDeclaredFields()) {
                from(field).ifPresent(annotated::add);
            }
            BeanInfo beanInfo = getBeanInfo(repeatClazz);
            for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                from(pd).ifPresent(annotated::add);
            }
            repeatClazz = repeatClazz.getSuperclass();
        }
        return annotated;
    }

    @Override
    public Optional<AnnotatedField> from(Field field) {
        if (field != null) {
            List<Annotation> annotations = filter(field.getDeclaredAnnotations());
            if (!annotations.isEmpty()) {
                return Optional.of(new AnnotatedField(field.getName(), field, annotations));
            }
        }
        return empty();
    }

    private Optional<AnnotatedMethod> from(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor != null && propertyDescriptor.getReadMethod() != null) {
            List<Annotation> annotations = filter(propertyDescriptor.getReadMethod().getAnnotations());
            if (!annotations.isEmpty()) {
                return Optional.of(new AnnotatedMethod(
                        propertyDescriptor.getName(),
                        propertyDescriptor.getReadMethod(),
                        annotations
                ));
            }
        }
        return empty();
    }

    private List<Annotation> filter(Annotation[] annotations) {
        return stream(annotations)
                .filter(a -> annotationClasses.contains(a.annotationType()))
                .collect(Collectors.toList());
    }

    private BeanInfo getBeanInfo(Class<?> clazz) throws DocumentFactoryException {
        try {
            return Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new DocumentFactoryException("BeanInfo retrieval failed.", e);
        }
    }

}
