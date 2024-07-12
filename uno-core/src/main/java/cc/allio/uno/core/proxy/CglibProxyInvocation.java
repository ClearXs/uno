package cc.allio.uno.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * cglib代理实现
 *
 * @author jiangw
 * @date 2021/8/12 16:53
 * @since 1.1
 */
public class CglibProxyInvocation implements ProxyInvocation {

    @Override
    public <T> T proxyInstance(ClassLoader classLoader, Class<T> target, InvocationInterceptor interceptor, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> {
            if (interceptor instanceof CglibInvocationInterceptor) {
                return ((CglibInvocationInterceptor) interceptor).invoke(obj, method, args1, proxy);
            }
            return interceptor.invoke(obj, method, args1);
        });
        Object proxy;
        if (args != null && args.length != 0) {
            Class<?>[] classes = objectToClass(args);
            // 对参数class化，双重验证判断（可能存在给定参数为null的请求）
            if (classes.length == args.length) {
                proxy = enhancer.create(classes, args);
                return target.cast(proxy);
            }
        }
        proxy = enhancer.create();
        return target.cast(proxy);
    }

    /**
     * {@code Object}类型的参数转为Class对象
     *
     * @param args 参数类型
     * @return class[] 对象
     */
    public Class<?>[] objectToClass(Object[] args) {
        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .map(Object::getClass)
                .collect(Collectors.toList())
                .toArray(new Class<?>[]{});
    }
}
