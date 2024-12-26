package cc.allio.uno.core.proxy;

/**
 * 代理策略
 *
 * @author j.x
 * @since 1.0
 */
interface ProxyInvocation {

	/**
	 * 创建实例
	 *
	 * @param classLoader 生成目标对象class文件的类加载器
	 * @param target      需要代理的class对象
	 * @param interceptor 统一方法拦截器
	 * @param args        方法参数
	 * @param <T>         目标对象类型
	 * @return 目标对象类型实例
	 * @throws InstantiationException 反射创建错误时抛出
	 * @throws IllegalAccessException 反射访问错误时抛出
	 */
	<T> T proxyInstance(ClassLoader classLoader, Class<T> target, InvocationInterceptor interceptor, Object[] args) throws InstantiationException, IllegalAccessException;

	/**
	 * 代理类全限定名称
	 *
	 * @param proxyName 代理对象名称
	 */
	default void proxyName(String proxyName) {

	}

}
