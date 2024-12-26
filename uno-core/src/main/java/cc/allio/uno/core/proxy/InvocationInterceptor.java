package cc.allio.uno.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <b>InvocationInterceptor是统一代理的具体实现</b>
 * <p>实现jdk、cglib、byte-buddy的代理实现</p>
 *
 * @author j.x
 * @since 1.1
 */
@FunctionalInterface
public interface InvocationInterceptor {

	/**
	 * 代理对象调用方法时进行的回调
	 *
	 * @param proxy  代理对象 or 增强的对象
	 * @param method 调用的方法
	 * @param args   方法参数
	 * @return 调用结果
	 * @throws InterruptedException      代理调用过程出现异常抛出
	 * @throws NoSuchMethodException     没有找到指定对象目标方法时抛出
	 * @throws InvocationTargetException 调用目标对象出现错误时候抛出
	 * @throws IllegalAccessException    方法非法访问时候抛出
	 */
	Object invoke(Object proxy, Method method, Object[] args) throws InterruptedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}
