package cc.allio.uno.core.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * cglib的代理实现
 *
 * @author j.x
 * @since 1.1
 */
public interface CglibInvocationInterceptor extends InvocationInterceptor {

	/**
	 * 代理对象调用方法时进行的回调
	 *
	 * @param object 代理对象 or 增强的对象
	 * @param method 调用的方法
	 * @param args   调用方法参数列表
	 * @param proxy  cglib方法拦截器
	 * @return 调用结果
	 * @throws InterruptedException      代理调用过程出现异常抛出
	 * @throws NoSuchMethodException     没有找到指定对象目标方法时抛出
	 * @throws InvocationTargetException 调用目标对象出现错误时候抛出
	 * @throws IllegalAccessException    方法非法访问时候抛出
	 * @see InvocationInterceptor#invoke(Object, Method, Object[])
	 */
	Object invoke(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable;

	/**
	 * 默认返回为空的代理调用
	 *
	 * @param proxy  代理对象 or 增强的对象
	 * @param method 调用的方法
	 * @param args   方法参数
	 * @return 默认为空
	 */
	@Override
	default Object invoke(Object proxy, Method method, Object[] args) {
		return null;
	}
}
