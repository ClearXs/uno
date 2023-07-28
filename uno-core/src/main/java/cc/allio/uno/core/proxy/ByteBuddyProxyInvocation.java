package cc.allio.uno.core.proxy;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.id.IdGenerator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

/**
 * <b>Byte Buddy是一款Java字节码增强框架，可以动态生成Java字节码文件，它他屏蔽了底层细节，提供统一易上手的api，简化字节码增加的难度，性能要强于jdk与cglib</b>
 *
 * @author jiangw
 * @date 2021/8/12 17:46
 * @since 1.1
 */
class ByteBuddyProxyInvocation implements ProxyInvocation {

    private static final String NAME = "Allio";

    private String proxyName;

    @Override
    public <T> T proxyInstance(ClassLoader classLoader,
                               Class<T> target,
                               InvocationInterceptor interceptor,
                               Object[] args) throws InstantiationException, IllegalAccessException {
        ByteBuddy byteBuddy = new ByteBuddy();
        // 指定生成目标class对象的地方
        // 即target包下，并且名称叫SCREW$16进制唯一字符串
        DynamicType.Builder<T> typeBuilder = byteBuddy
                .subclass(target);
        if (StringUtils.isEmpty(proxyName)) {
            typeBuilder = typeBuilder.name(target.getPackage().getName()
                    .concat(StringPool.DOT)
                    .concat(NAME)
                    .concat(StringPool.DOLLAR)
                    .concat(IdGenerator.defaultGenerator().getNextIdAsString()));
        } else {
            typeBuilder = typeBuilder.name(proxyName);
        }
        // 定义InvocationInterceptor类型的字段
        DynamicType.Builder.MethodDefinition.ReceiverTypeDefinition<T> newBuilder = typeBuilder
                .defineField("interceptor", InvocationInterceptor.class, Modifier.PRIVATE)
                .implement(SetterGetterInterceptor.class)
                .intercept(FieldAccessor.ofBeanProperty());
        // 指定拦截的方法
        Method[] declaredMethods = target.getDeclaredMethods();
        Class<?> methodInterceptor = target.isInterface() ? ByteBuddyInterfaceMethodInterceptor.class : ByteBuddyMethodInterceptor.class;
        for (Method method : declaredMethods) {
            newBuilder = newBuilder.method(
                    // 匹配方法名
                    ElementMatchers.named(method.getName())
                            // 匹配方法返回类型
                            .and(ElementMatchers.returns(method.getReturnType())
                                    // 匹配方法原始参数
                                    .and(ElementMatchers.takesArguments(method.getParameterTypes())))
            ).intercept(MethodDelegation.to(methodInterceptor));
        }
        T proxy = newBuilder.make().load(classLoader).getLoaded().newInstance();
        try {
            // InvocationInterceptor类型字段interceptor赋值
            proxy.getClass().getMethod("setInterceptor", InvocationInterceptor.class).invoke(proxy, interceptor);
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new NullPointerException("setInterceptor filed failed " + e.getMessage());
        }
        return proxy;
    }

    /**
     * byte buddy方法拦截器，代理对象方法调用时进行拦截，针对接口类型进行拦截
     *
     * @author jiangw
     * @date 2021/8/13 14:38
     * @since 1.1
     */
    public static class ByteBuddyInterfaceMethodInterceptor {

        private ByteBuddyInterfaceMethodInterceptor() {
        }

        @RuntimeType
        public static Object intercept(@FieldValue("interceptor") InvocationInterceptor interceptor,
                                       @This Object proxy,
                                       @Origin Method method,
                                       @AllArguments Object[] args) throws InterruptedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            if (interceptor == null) {
                return null;
            }
            return interceptor.invoke(proxy, method, args);
        }
    }

    /**
     * byte buddy方法拦截器，代理对象方法调用时进行拦截
     *
     * @author jiangw
     * @date 2021/8/13 10:33
     * @since 1.1
     */
    public static class ByteBuddyMethodInterceptor {

        private ByteBuddyMethodInterceptor() {

        }

        /**
         * 具体的方法拦截是一个静态方法，在byte buddy动态生成的class文件中，具体方法的调用是ByteBuddyMethodInterceptor.intercept进行调用，所以需要是一个静态方法
         * <p>@RuntimeType注解定义运行时目标方法</p>
         *
         * @param interceptor @FieldValue注解注入代理对象的提供字段
         * @param proxy       @This注解提供当前被拦截、动态生成的对象
         * @param method      @Origin注解
         * @param args        @AllArguments注解绑定所有的原方法的参数
         * @return 调用结果
         */
        @RuntimeType
        public static Object intercept(@FieldValue("interceptor") InvocationInterceptor interceptor,
                                       @This Object proxy,
                                       @Origin Method method,
                                       @AllArguments Object[] args,
                                       @SuperCall Callable<?> callable) throws Throwable {
            if (interceptor == null) {
                return null;
            }
            if (interceptor instanceof ByteBuddyInvocationInterceptor) {
                return ((ByteBuddyInvocationInterceptor) interceptor).invoke(proxy, method, args, callable);
            }
            return interceptor.invoke(proxy, method, args);
        }
    }

    @Override
    public void proxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    /**
     * ByteBuddy 调用set get方法时候的拦截器
     */
    interface SetterGetterInterceptor {

        /**
         * 获取InvocationInterceptor
         *
         * @return InvocationInterceptor实例
         */
        InvocationInterceptor getInterceptor();

        /**
         * 设置InvocationInterceptor
         *
         * @param interceptor 设置该实例
         */
        void setInterceptor(InvocationInterceptor interceptor);
    }

}
