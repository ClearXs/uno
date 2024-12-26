package cc.allio.uno.core.proxy;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

/**
 * <b>慎用</b><br/>
 * 使用{@link ComposeSharable}注解标识代理对象方法时，将会使用调用composable的方法进行调用<br/>
 * 使用{@link ComposeOrigin}注解标识某个方法时，如果调用的对象是经过{@link ComposableInvocationInterceptor}方法拦截的代理对象时
 * 将会追踪他的调用栈，获取调用栈的类Class对象，判断他的方法上是否有使用到这个注解，只要调用栈里有一个使用就判定调用原声对象的方法。<br/>
 *
 * @author j.x
 * @since 1.0
 */
@Slf4j
public class ComposableInvocationInterceptor implements CglibInvocationInterceptor {


    public final Object composable;

    public ComposableInvocationInterceptor(Object composable) {
        this.composable = composable;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        ComposeSharable sharable = AnnotationUtils.findAnnotation(method, ComposeSharable.class);
        if (sharable != null) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            // 获取调用栈中是否有调用方法字段是否使用@ComposeOrigin注解
            boolean traceMacheComposeOrigin = Arrays.stream(stackTrace)
                    .anyMatch(trace -> {
                        Class<?> traceClass = AccessController.doPrivileged((PrivilegedAction<Class<?>>) () -> {
                            try {
                                return Class.forName(
                                        trace.getClassName(),
                                        false,
                                        Thread.currentThread().getContextClassLoader());
                            } catch (ClassNotFoundException e) {
//                                log.error("ComposableInvocationInterceptor can't invoke stack, classname for: {}", trace.getClassName());
                            }
                            return null;
                        });
                        if (traceClass != null) {
                            return Arrays.stream(traceClass.getDeclaredMethods())
                                    // 调用栈的方法名与类的Class方法名做比较过滤。
                                    .filter(traceMethod -> traceMethod.getName().equals(trace.getMethodName()))
                                    .flatMap(traceMethod ->
                                            // 二维数组，一维字段类型，二维字段上的注解
                                            Arrays.stream(traceMethod.getParameterAnnotations())
                                                    .flatMap(Arrays::stream))
                                    .anyMatch(annotation -> AnnotationUtils.getAnnotation(annotation, ComposeOrigin.class) != null);
                        }
                        return false;
                    });
            if (traceMacheComposeOrigin) {
                // 如果存在@ComposeOrigin，调用组合类型对应的方法
                Method composeMethod = MethodUtils.getAccessibleMethod(composable.getClass(), method.getName(), method.getParameterTypes());
                return composeMethod.invoke(composable, args);
            }
            return proxy.invokeSuper(object, args);
        }
        return proxy.invokeSuper(object, args);
    }

}
