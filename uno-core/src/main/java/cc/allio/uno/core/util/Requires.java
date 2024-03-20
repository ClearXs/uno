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
     * @throws NullPointerException 对象为null时，抛出异常
     */
    public static void isNotNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof String sv && StringUtils.isEmpty(sv)) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Collection<?> coll && CollectionUtils.isEmpty(coll)) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Map<?, ?> map && CollectionUtils.isEmpty(map)) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Integer i && i == 0) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Long l && l == 0L) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Double d && d == 0) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Float f && f == 0) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
        if (obj instanceof Boolean bool && Boolean.FALSE.equals(bool)) {
            throw new NullPointerException("requires: " + message + " is empty");
        }
    }
}
