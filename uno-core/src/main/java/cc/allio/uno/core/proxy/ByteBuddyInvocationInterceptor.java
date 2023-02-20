package cc.allio.uno.core.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 针对子类继承的拦截器
 *
 * @author jiangw
 * @date 2021/8/13 10:56
 * @since 1.0
 */
public interface ByteBuddyInvocationInterceptor extends InvocationInterceptor {

    /**
     * 代理对象调用方法时进行的回调
     *
     * @param proxy    代理对象 or 增强的对象
     * @param method   调用的方法
     * @param args     调用方法参数列表
     * @param callable 可调用父类方法
     * @return 代理调用完成的结果
     * @throws Throwable 方法拦截调用过程中错误出现任何错误时抛出
     * @see #invoke(Object, Method, Object[])
     */
    Object invoke(Object proxy, Method method, Object[] args, Callable<?> callable) throws Throwable;

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
