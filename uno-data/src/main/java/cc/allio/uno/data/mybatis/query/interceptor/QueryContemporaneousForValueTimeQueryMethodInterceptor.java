package cc.allio.uno.data.mybatis.query.interceptor;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.mybatis.mapper.QueryMapper;
import cc.allio.uno.data.query.stream.ContemporaneousStream;
import cc.allio.uno.data.query.stream.ContemporaneousValueTimeStream;
import cc.allio.uno.data.mybatis.query.stream.QueryMethodInterceptorStream;
import cc.allio.uno.core.aop.JoinPointDelegate;

import java.lang.reflect.Method;

/**
 * {@link QueryMapper#queryContemporaneousForValueTime(QueryFilter)} 方法拦截器，调用{@link QueryContemporaneousQueryMethodInterceptor}结果进行封装
 *
 * @author jiangwei
 * @date 2022/11/18 13:11
 * @since 1.1.0
 */
public class QueryContemporaneousForValueTimeQueryMethodInterceptor implements QueryMethodInterceptorDelegate {

    QueryMethodInterceptor delegate;

    static final Method QUERY_LIST_CONTEMPORANEOUS_FOR_VALUE_TIME;

    static {
        try {
            QUERY_LIST_CONTEMPORANEOUS_FOR_VALUE_TIME = QueryMapper.class.getMethod("queryContemporaneousForValueTime", QueryFilter.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public QueryContemporaneousForValueTimeQueryMethodInterceptor(QueryMethodInterceptor delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(JoinPointDelegate point) throws Throwable {
        return new ContemporaneousValueTimeStream(
                new ContemporaneousStream(
                        new QueryMethodInterceptorStream<>(delegate, point)))
                .read(point.getArg(QueryFilter.class));
    }

    @Override
    public Method getMethod() {
        return QUERY_LIST_CONTEMPORANEOUS_FOR_VALUE_TIME;
    }

    @Override
    public QueryMethodInterceptor getDelegate() {
        return delegate;
    }
}
