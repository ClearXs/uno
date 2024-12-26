package cc.allio.uno.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 不产生任何jdk反射能力的方法调用拦截器
 *
 * @author j.x
 * @since 1.0.6
 */
public class IncapacityInvocationInterceptor implements InvocationInterceptor {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return method.invoke(proxy, args);
    }
}
