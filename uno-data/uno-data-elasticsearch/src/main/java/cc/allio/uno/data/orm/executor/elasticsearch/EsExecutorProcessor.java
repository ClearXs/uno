package cc.allio.uno.data.orm.executor.elasticsearch;

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
 * {@link cc.allio.uno.data.orm.executor.ExecutorAware}的ES实现
 *
 * @author jiangwei
 * @date 2024/1/10 22:26
 * @since 1.1.6
 */
public class EsExecutorProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ClassUtils.isAssignable(EsExecutorAware.class, bean.getClass())) {
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.addAspect(EsExecutorAspect.class);
            NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
            pointcut.setMappedName("getExecutor");
            MethodInterceptor methodInterceptor = invocation -> CommandExecutorFactory.getDSLExecutor(ExecutorKey.ELASTICSEARCH);
            proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
            return proxyFactory.getProxy();
        }
        return bean;
    }
}
