package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.data.orm.executor.CommandExecutorAware;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * {@link CommandExecutorAware}实现
 *
 * @author j.x
 * @date 2024/1/10 18:35
 * @since 1.1.7
 */
public class DbCommandExecutorProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!ClassUtils.isAssignable(DbCommandExecutorAware.class, bean.getClass())) {
            return bean;
        }
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAspect(DbCommandExecutorAspect.class);
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("getExecutor");
        MethodInterceptor methodInterceptor = k -> CommandExecutorFactory.getDSLExecutor(ExecutorKey.DB);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
        return proxyFactory.getProxy();
    }
}
