package cc.allio.uno.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 一些关于请求验证的方法
 *
 * @author j.x
 */
public final class Requires {

    /**
     * 批量判断请求对象是否为空
     *
     * @param objs 对象数组
     */
    public static void isNotNulls(Object... objs) {
        Arrays.stream(objs).forEach(obj -> isNotNull(objs, ""));
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
    }
}
