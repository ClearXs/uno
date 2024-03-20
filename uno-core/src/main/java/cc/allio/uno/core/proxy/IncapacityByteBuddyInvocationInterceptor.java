package cc.allio.uno.core.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 不产生任何Bytebuddy反射能力的方法拦截器
 *
 * @author j.x
 * @date 2022/3/31 18:52
 * @since 1.0.6
 */
public class IncapacityByteBuddyInvocationInterceptor implements ByteBuddyInvocationInterceptor {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args, Callable<?> callable) throws Throwable {
        return method.invoke(proxy, args);
    }
}
