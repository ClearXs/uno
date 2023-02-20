package cc.allio.uno.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 一些关于请求验证的方法
 *
 * @author jw
 * @date 2021/12/6 13:57
 */
public class Requires {

    /**
     * 批量判断请求对象是否为空
     *
     * @param objs 对象数组
     */
    public static void isNotNulls(Object... objs) {
        Arrays.stream(objs)
                .forEach(obj -> isNotNull(objs, ""));
    }

    /**
     * 判断对象是不是null，如果是null则会抛出异常
     *
     * @param obj     需要判断的对象
     * @param message 异常的消息内容
     * @throws IllegalArgumentException 对象为null时，抛出异常
     */
    public static void isNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException("requires: " + message + " is empty");
        }
        if (obj instanceof String) {
            if (StringUtils.isEmpty((String) obj)) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Collection) {
            if (Collections.isEmpty((Collection<?>) obj)) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Map) {
            if (Collections.isEmpty((Map<?, ?>) obj)) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Integer) {
            if ((Integer) obj == 0) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Long) {
            if ((Long) obj == 0L) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Double) {
            if ((Double) obj == 0) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Float) {
            if ((Float) obj == 0) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
        if (obj instanceof Boolean) {
            if (Boolean.FALSE.equals((obj))) {
                throw new IllegalArgumentException("requires: " + message + " is empty");
            }
        }
    }
}
