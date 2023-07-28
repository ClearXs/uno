package cc.allio.uno.data.query.mybatis.query.interceptor;

import com.google.common.collect.Maps;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 查询拦截器静态工厂
 *
 * @author jiangwei
 * @date 2022/9/30 16:59
 * @since 1.1.0
 */
public class QueryMethodInterceptorFactory {

    private static final Map<InterceptorMethod, QueryMethodInterceptor> cache = Maps.newHashMap();

    /**
     * 注册查询方法拦截器
     *
     * @param queryMethodInterceptor 查询方法拦截器实例
     */
    public static synchronized void registry(QueryMethodInterceptor queryMethodInterceptor) {
        cache.put(new InterceptorMethod(queryMethodInterceptor.getMethod()), queryMethodInterceptor);
    }

    /**
     * 从缓存中获取对应方法的拦截器
     *
     * @param method 某个指定的方法
     * @return 拦截器实例
     */
    public static synchronized QueryMethodInterceptor create(Method method) {
        return cache.get(new InterceptorMethod(method));
    }

    /**
     * 拦截作用的方法，用于找到{@link QueryMethodInterceptor}
     */
    @Data
    static class InterceptorMethod {
        private final Class<?> returnType;
        private final String methodName;
        private final Class<?>[] parameterTypes;
        private final Method originMethod;

        public InterceptorMethod(Method method) {
            this.returnType = method.getReturnType();
            this.methodName = method.getName();
            this.parameterTypes = method.getParameterTypes();
            this.originMethod = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InterceptorMethod that = (InterceptorMethod) o;
            return Objects.equals(returnType, that.returnType) && Objects.equals(methodName, that.methodName) && Arrays.equals(parameterTypes, that.parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(returnType, methodName);
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }
    }
}
