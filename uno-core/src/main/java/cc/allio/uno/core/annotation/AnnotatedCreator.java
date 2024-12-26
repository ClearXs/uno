package cc.allio.uno.core.annotation;

import cc.allio.uno.core.annotation.document.DocumentFactoryException;
import com.google.common.collect.ListMultimap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;


/**
 * {@link Annotated}生成器
 *
 * @author j.x
 * @since 1.1.2
 */
public interface AnnotatedCreator {

    /**
     * 根据指定对象创建注解的元数据对象{@link Annotated}
     *
     * @param clazz 对象clazz
     * @return 注解元数据列表
     * @throws DocumentFactoryException
     */
    ListMultimap<Class<? extends Annotation>, Annotated> index(Class<?> clazz) throws DocumentFactoryException;

    /**
     * 根据指定的注解对象获取注解列表中的注解元数据对象{@link Annotated}
     *
     * @param index           注解元数据列表
     * @param annotationClass 指定的注解对象
     * @return 注解元数据对象 or null
     * @see #index(Class)
     */
    Annotated oneOrNull(ListMultimap<Class<? extends Annotation>, Annotated> index, Class<? extends Annotation> annotationClass) throws DocumentFactoryException;

    /**
     * 根据指定对象创建元数据列表
     *
     * @param clazz 对象clazz
     * @return 注解列表元数据
     * @throws DocumentFactoryException
     */
    List<Annotated> annotatedFrom(Class<?> clazz) throws DocumentFactoryException;

    /**
     * 根据{@link Field}获取{@link AnnotatedField}对象
     *
     * @param field {@link Field}对象
     * @return optional
     */
    Optional<AnnotatedField> from(Field field);

}
