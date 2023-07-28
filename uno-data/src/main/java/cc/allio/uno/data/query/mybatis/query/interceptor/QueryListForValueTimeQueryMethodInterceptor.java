package cc.allio.uno.data.query.mybatis.query.interceptor;

import cc.allio.uno.data.query.mybatis.mapper.QueryMapper;
import cc.allio.uno.data.query.mybatis.QueryFilter;
import cc.allio.uno.data.query.mybatis.query.stream.QueryMethodInterceptorStream;
import cc.allio.uno.data.query.stream.ValueTime;
import cc.allio.uno.data.query.stream.ValueTimeStream;
import cc.allio.uno.core.aop.JoinPointDelegate;

import java.lang.reflect.Method;

/**
 * {@link QueryMapper#queryListForValueTime(QueryFilter)} 方法拦截器，调用{@link QueryListQueryMethodInterceptor}结果进行{@link ValueTime}的封装
 *
 * @author jiangwei
 * @date 2022/11/18 13:10
 * @since 1.1.0
 */
final class QueryListForValueTimeQueryMethodInterceptor implements QueryMethodInterceptorDelegate {

    QueryMethodInterceptor delegate;

    static final Method QUERY_LIST_FOR_VALUE_TIME;

    static {
        try {
            QUERY_LIST_FOR_VALUE_TIME = QueryMapper.class.getMethod("queryListForValueTime", QueryFilter.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public QueryListForValueTimeQueryMethodInterceptor(QueryMethodInterceptor delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(JoinPointDelegate point) throws Throwable {
        return new ValueTimeStream(
                new QueryMethodInterceptorStream<>(delegate, point))
                .read(point.getArg(QueryFilter.class));
    }

    @Override
    public Method getMethod() {
        return QUERY_LIST_FOR_VALUE_TIME;
    }

    @Override
    public QueryMethodInterceptor getDelegate() {
        return delegate;
    }
}
