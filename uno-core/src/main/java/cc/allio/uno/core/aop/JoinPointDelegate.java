package cc.allio.uno.core.aop;

import org.aspectj.lang.JoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class JoinPointDelegate {

    private final JoinPoint point;

    public JoinPointDelegate(MethodInvocationProceedingJoinPoint point) {
        this.point = point;
    }

    /**
     * 调用{@link MethodInvocationProceedingJoinPoint#getArgs()}方法获取参数列表
     *
     * @return 调用参数列表
     */
    public Object[] getArgs() {
        return point.getArgs();
    }

    /**
     * 获取代理对象
     *
     * @return 代理对象实例
     */
    public Object getTarget() {
        return point.getTarget();
    }

    /**
     * 从切入点{@link MethodInvocationProceedingJoinPoint}获取具体调用的方法
     *
     * @return 方法实例
     * @throws IllegalStateException 获取失败时抛出
     */
    public Method getMethod() {
        String name = point.getSignature().getName();
        Class<?>[] argClasses = Arrays.stream(point.getArgs())
                .map(Object::getClass)
                .toList()
                .toArray(new Class<?>[]{});
        // 尝试知道获取AOP原始Class对象
        Class<?> maybeOriginClass = AopUtils.getTargetClass(point.getTarget());
        return ClassUtils.getMethod(maybeOriginClass, name, argClasses);
    }

    /**
     * 从{@link MethodInvocationProceedingJoinPoint}中获取具体的参数实例，如果给定的类型具有多个的话，取第一个
     *
     * @param type 获取的参数类型
     * @param <T>  参数范型
     * @return 参数实例或者null
     */
    public <T> T getArg(Class<T> type) {
        Object[] args = point.getArgs();
        for (Object arg : args) {
            if (type.isAssignableFrom(arg.getClass())) {
                return type.cast(arg);
            }
        }

        return null;
    }

    /**
     * 根据指定的索引从{@link MethodInvocationProceedingJoinPoint}获取具体的参数
     *
     * @param index 索引位置
     * @param <T>   需要的范型
     * @return 参数实例或者null
     * @throws ArrayIndexOutOfBoundsException 给定索引超过数组下标时抛出
     */
    public <T> T getArg(int index) {
        return (T) point.getArgs()[index];
    }

    /**
     * 替换指定索引位置参数
     *
     * @param index 索引未知
     * @param arg   参数
     */
    public void replace(int index, Object arg) {
        Object[] args = point.getArgs();
        args[index] = arg;
    }
}
