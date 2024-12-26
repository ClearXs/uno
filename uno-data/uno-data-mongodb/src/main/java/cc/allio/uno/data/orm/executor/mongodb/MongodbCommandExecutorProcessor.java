package cc.allio.uno.data.orm.executor.mongodb;

import cc.allio.uno.core.util.ClassUtils;
import cc.allio.uno.data.orm.executor.CommandExecutorFactory;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * handle take on {@link MongodbCommandExecutorAware} interface object, set proxy obtain {@link MongodbCommandExecutor}
 *
 * @author j.x
 * @see MongodbCommandExecutorAware
 * @since 1.1.7
 */
public class MongodbCommandExecutorProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ClassUtils.isAssignable(MongodbCommandExecutorAware.class, bean.getClass())) {
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.addAspect(MongodbCommandExecutorAspect.class);
            NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
            pointcut.setMappedName("getExecutor");
            MethodInterceptor methodInterceptor = invocation -> CommandExecutorFactory.getDSLExecutor(ExecutorKey.MONGODB);
            proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
            return proxyFactory.getProxy();
        }
        return bean;
    }
}
