package cc.allio.uno.core.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * field util
 *
 * @author jiangwei
 * @date 2024/1/9 16:12
 * @since 1.1.6
 */
public class FieldUtils extends org.apache.commons.lang3.reflect.FieldUtils {

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls the {@link Class} to query
     * @return an array of Fields (possibly empty).
     * @throws NullPointerException if the class is {@code null}
     * @since 3.2
     */
    public static Field[] getAllFields(final Class<?> cls) {
        return getAllFieldsList(cls).toArray(ArrayUtils.EMPTY_FIELD_ARRAY);
    }

    /**
     * Gets all fields of the given class and its parents (if any).
     *
     * @param cls the {@link Class} to query
     * @return a list of Fields (possibly empty).
     * @throws NullPointerException if the class is {@code null}
     * @since 3.2
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Objects.requireNonNull(cls, "cls");
        final List<Field> allFields = new ArrayList<>();
        recursionGetAllField(cls, allFields);
        return allFields;
    }

    private static void recursionGetAllField(Class<?> cls, List<Field> fieldSet) {
        Class<?> superclass = cls.getSuperclass();
        if (superclass != null) {
            recursionGetAllField(superclass, fieldSet);
        }
        final Field[] declaredFields = cls.getDeclaredFields();
        Collections.addAll(fieldSet, declaredFields);
    }
}
