package cc.allio.uno.core.proxy;

import java.lang.reflect.Proxy;

/**
 * jdk代理对象实现
 *
 * @author j.x
 * @since 1.1
 */
class JDKProxyInvocation implements ProxyInvocation {

	@Override
	public <T> T proxyInstance(ClassLoader classLoader, Class<T> target, InvocationInterceptor interceptor, Object[] args) {
		Class<?>[] interfaces = target.isInterface() ? new Class[]{target} : target.getInterfaces();
		Object proxy = Proxy.newProxyInstance(classLoader, interfaces, interceptor::invoke);
		return target.cast(proxy);
	}
}
