package cc.allio.uno.data.query.db.query.interceptor;

/**
 * 拦截器代理接口
 *
 * @author j.x
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
