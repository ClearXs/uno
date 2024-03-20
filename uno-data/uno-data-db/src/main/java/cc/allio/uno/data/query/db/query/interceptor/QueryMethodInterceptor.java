package cc.allio.uno.data.query.db.query.interceptor;

import cc.allio.uno.core.aop.JoinPointDelegate;

import java.lang.reflect.Method;

/**
 * 定义查询方法拦截器，基于AOP实现
 *
 * @author j.x
 * @date 2022/9/30 16:29
 * @since 1.1.0
 */
public interface QueryMethodInterceptor {

    /**
     * 方法调用
     *
     * @param point 方法切入点对象
     * @return 方法返回对象
     * @throws Throwable 方法调用过程抛出异常
     */
    Object invoke(JoinPointDelegate point) throws Throwable;

    /**
     * 获取当前拦截方法
     *
     * @return 方法实例
     */
    Method getMethod();
}
