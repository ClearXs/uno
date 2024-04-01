package cc.allio.uno.data.orm.executor.influxdb;

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
 * handle implement {@link InfluxdbCommandExecutorAware}, set a proxy obtain get {@link InfluxdbCommandExecutor}
 *
 * @author j.x
 * @date 2024/4/1 17:23
 * @since 1.1.8
 */
public class InfluxdbCommandExecutorProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ClassUtils.isAssignable(InfluxdbCommandExecutorAware.class, bean.getClass())) {
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            proxyFactory.setTarget(bean);
            proxyFactory.addAspect(InfluxdbCommandExecutorAspect.class);
            NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
            pointcut.setMappedName("getExecutor");
            MethodInterceptor methodInterceptor = invocation -> CommandExecutorFactory.getDSLExecutor(ExecutorKey.INFLUXDB);
            proxyFactory.addAdvisor(new DefaultPointcutAdvisor(pointcut, methodInterceptor));
            return proxyFactory.getProxy();
        }
        return bean;
    }
}
