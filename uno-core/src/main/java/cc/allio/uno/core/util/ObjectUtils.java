package cc.allio.uno.core.util;

import org.springframework.lang.Nullable;

/**
 * Object的实用方法，基于{@link org.springframework.util.ObjectUtils}
 *
 * @author jiangwei
 * @date 2022/1/29 16:02
 * @since 1.0
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    /**
     * 基于{@link #isEmpty(Object)}取反
     *
     * @param obj obj
     * @return true or false
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 基于{@link #isEmpty(Object[])}取反
     *
     * @param array array
     * @return true or false
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return !isEmpty(array);
    }
}
