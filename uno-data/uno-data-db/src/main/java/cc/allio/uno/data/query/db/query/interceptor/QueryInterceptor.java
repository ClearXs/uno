package cc.allio.uno.data.query.db.query.interceptor;

import cc.allio.uno.core.aop.JoinPointDelegate;
import cc.allio.uno.data.query.db.mapper.QueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 查询拦截器实现
 *
 * @author j.x
 * @date 2022/9/30 16:46
 * @since 1.1.0
 */
@Slf4j
@Aspect
public class QueryInterceptor {

    public QueryInterceptor() {
        // #queryList拦截
        QueryMethodInterceptor queryListMethodInterceptor = new QueryListQueryMethodInterceptor();
        QueryMethodInterceptorFactory.registry(queryListMethodInterceptor);
        // #queryContemporaneous拦截
        QueryMethodInterceptor queryContemporaneousMethodInterceptor = new QueryContemporaneousQueryMethodInterceptor(queryListMethodInterceptor);
        QueryMethodInterceptorFactory.registry(queryContemporaneousMethodInterceptor);
        // #queryListForValueTime拦截
        QueryMethodInterceptor queryListQueryMethodInterceptor = new QueryListForValueTimeQueryMethodInterceptor(queryListMethodInterceptor);
        QueryMethodInterceptorFactory.registry(queryListQueryMethodInterceptor);
        // #queryContemporaneousForValueTime拦截
        QueryMethodInterceptor queryContemporaneousForValueTimeQueryMethodInterceptor = new QueryContemporaneousForValueTimeQueryMethodInterceptor(queryListMethodInterceptor);
        QueryMethodInterceptorFactory.registry(queryContemporaneousForValueTimeQueryMethodInterceptor);
    }

    /**
     * 切入点为{@link QueryMapper}下的所有方法
     */
    @Pointcut("execution(* cc.allio.uno.data.query.db.mapper.QueryMapper.*(..))")
    public void interceptor() {
    }

    /**
     * 拦截{@link QueryMapper}方法的调用
     *
     * @param point 切入点对象
     * @return 方法调用结果
     * @throws Throwable 调用过程阿婆出异常
     */
    @Around(value = "interceptor()")
    public Object inject(ProceedingJoinPoint point) throws Throwable {
        if (point instanceof MethodInvocationProceedingJoinPoint) {
            MethodInvocationProceedingJoinPoint methodPoint = (MethodInvocationProceedingJoinPoint) point;
            JoinPointDelegate pointDelegate = new JoinPointDelegate(methodPoint);
            try {
                // 从指定的Query方法获取对应的拦截器
                Method method = pointDelegate.getMethod();
                QueryMethodInterceptor queryMethodInterceptor = QueryMethodInterceptorFactory.create(method);
                if (queryMethodInterceptor != null) {
                    return queryMethodInterceptor.invoke(pointDelegate);
                }
            } catch (NoSuchMethodException | IllegalStateException ex) {
                // 没有找到指定的方法时会抛出此异常，其原因可能是某个代理对象没有暴露出class对象，没有找到方法名
                log.error("Mapper Interceptor can't such method: {}", ex.getMessage());
            }

            return point.proceed();
        }
        return point.proceed();
    }

}
