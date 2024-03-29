package cc.allio.uno.core.annotation;

import cc.allio.uno.core.annotation.document.DocumentFactoryException;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Represents an annotated field or method found in introspection.
 */
public interface Annotated {

    /**
     * Returns the name of the field or method. In case of methods,
     * the name is according to the {@link java.beans.PropertyDescriptor}.
     *
     * @return the name
     */
    String getName();

    /**
     * Returns a description to be identified in exception messages.
     *
     * @return a description
     */
    String getDescription();

    /**
     * Returns all GeoJson annotations from this member.
     *
     * @return all GeoJson annotations
     */
    List<Annotation> getAnnotations();

    /**
     * 返回当前成员指定对象注解
     *
     * @param annoClass 指定注解类型
     * @param <T>       注解类型
     */
    default <T extends Annotation> T getAnno(Class<T> annoClass) {
        return (T) getAnnotations().stream()
                .filter(annotation -> annoClass.isAssignableFrom(annotation.getClass()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the value of a the annotated member.
     *
     * @param object        the object of which the value should be returned
     * @param expectedClass the expected type of value
     * @param <T>           the class of the expected type
     * @return the value, may be <code>null</code>
     * @throws DocumentFactoryException on any error
     */
    <T> T getValue(Object object, Class<T> expectedClass) throws DocumentFactoryException;
}
