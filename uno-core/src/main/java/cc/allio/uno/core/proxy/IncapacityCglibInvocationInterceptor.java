package cc.allio.uno.core.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 不产生任何Cglib反射能力的方法调用拦截器
 *
 * @author j.x
 * @since 1.0.6
 */
public class IncapacityCglibInvocationInterceptor implements CglibInvocationInterceptor {
    @Override
    public Object invoke(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invokeSuper(object, args);
    }
}
