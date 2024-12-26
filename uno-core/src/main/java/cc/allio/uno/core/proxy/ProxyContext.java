package cc.allio.uno.core.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * 代理策略的上下文类，用来管理不同代理实现
 *
 * @author j.x
 * @since 1.1
 */
class ProxyContext {

	private final Map<Class<? extends ProxyInvocation>, ProxyInvocation> cache = new HashMap<>();

	private String proxyName;

	/**
	 * @see #proxyInstance(ClassLoader, Class, InvocationInterceptor, boolean)
	 */
	public <T> T proxyInstance(Class<T> tClass, InvocationInterceptor interceptor) {
		return proxyInstance(Thread.currentThread().getContextClassLoader(), tClass, interceptor);
	}

	/**
	 * @see #proxyInstance(ClassLoader, Class, InvocationInterceptor, boolean)
	 */
	public <T> T proxyInstance(ClassLoader classLoader, Class<T> tClass, InvocationInterceptor interceptor) {
		return proxyInstance(classLoader, tClass, interceptor, false);
	}

	/**
	 * @see #proxyInstance(ClassLoader, Class, InvocationInterceptor, boolean, Object[])
	 */
	public <T> T proxyInstance(ClassLoader classLoader, Class<T> tClass, InvocationInterceptor interceptor, boolean slowly) {
		return proxyInstance(classLoader, tClass, interceptor, slowly, null);
	}

	/**
	 * <b>创建代理实例</b>
	 * <p>使用什么框架进行代理它将根据tClass参数与fast参数进行判断：逻辑如下：</p>
	 * <p>1.如果fast为true，将使用ByteBuddy</p>
	 * <p>2.如果fast为false并且tClass是接口，那么使用jdk代理</p>
	 * <p>3.如果fast为false并且tClass不是接口，那么使用cglib进行代理</p>
	 *
	 * @param classLoader 类加载器，用来加载创建的代理class对象
	 * @param tClass      需要代理的class对象
	 * @param interceptor 调用时的拦截器
	 * @param slowly      是否使用性能更好的byte buddy
	 * @param args        构造方法参数
	 * @param <T>         目标对象的类型
	 * @return 返回创建的目标对象的实例
	 * @throws NullPointerException 当代理对象创建失败时抛出该异常
	 */
	public <T> T proxyInstance(ClassLoader classLoader, Class<T> tClass, InvocationInterceptor interceptor, boolean slowly, Object[] args) {
		try {
			if (slowly) {
				ProxyInvocation invocation = getInterceptor(ByteBuddyProxyInvocation.class);
				invocation.proxyName(proxyName);
				return invocation.proxyInstance(ClassLoader.getSystemClassLoader(), tClass, interceptor, args);
			}
			// 判断使用cglib还是jdk
			if (tClass.isInterface()) {
				return getInterceptor(JDKProxyInvocation.class).proxyInstance(classLoader, tClass, interceptor, args);
			}
			return getInterceptor(CglibProxyInvocation.class).proxyInstance(classLoader, tClass, interceptor, args);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new NullPointerException("can't proxy error: " + e.getMessage());
		}
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	private ProxyInvocation getInterceptor(Class<? extends ProxyInvocation> clazz) throws InstantiationException, IllegalAccessException {
		ProxyInvocation proxyInvocation = cache.get(clazz);
		if (proxyInvocation == null) {
			proxyInvocation = clazz.newInstance();
		}
		cache.put(clazz, proxyInvocation);
		return proxyInvocation;
	}
}
