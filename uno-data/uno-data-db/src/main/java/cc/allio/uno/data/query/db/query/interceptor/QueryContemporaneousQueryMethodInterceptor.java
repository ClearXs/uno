package cc.allio.uno.data.query.db.query.interceptor;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.db.mapper.QueryMapper;
import cc.allio.uno.data.query.stream.ContemporaneousStream;
import cc.allio.uno.data.query.db.query.stream.QueryMethodInterceptorStream;
import cc.allio.uno.core.aop.JoinPointDelegate;

import java.lang.reflect.Method;

/**
 * {@link QueryMapper#queryContemporaneous(QueryFilter)}方法拦截器
 *
 * @author j.x
 * @since 1.1.0
 */
final class QueryContemporaneousQueryMethodInterceptor implements QueryMethodInterceptorDelegate {
    QueryMethodInterceptor delegate;

    public QueryContemporaneousQueryMethodInterceptor(QueryMethodInterceptor delegate) {
        this.delegate = delegate;
    }

    static final Method QUERY_CONTEMPORANEOUS;

    static {
        try {
            QUERY_CONTEMPORANEOUS = QueryMapper.class.getMethod("queryContemporaneous", QueryFilter.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(JoinPointDelegate point) throws Throwable {
        return new ContemporaneousStream(
                new QueryMethodInterceptorStream<>(delegate, point))
                .read(point.getArg(QueryFilter.class));
    }

    @Override
    public Method getMethod() {
        return QUERY_CONTEMPORANEOUS;
    }

    @Override
    public QueryMethodInterceptor getDelegate() {
        return delegate;
    }
}
