package cc.allio.uno.data.query.mybatis.query.interceptor;

/**
 * 拦截器代理接口
 *
 * @author jiangwei
 * @date 2022/9/30 17:00
 * @since 1.1.0
 */
public interface QueryMethodInterceptorDelegate extends QueryMethodInterceptor {

    /**
     * 获取代理
     *
     * @return
     */
    QueryMethodInterceptor getDelegate();
}
