package cc.allio.uno.data.mybatis.query.stream;

import cc.allio.uno.data.mybatis.query.interceptor.QueryMethodInterceptor;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.core.aop.JoinPointDelegate;
import cc.allio.uno.data.query.stream.CollectionTimeStream;
import reactor.core.publisher.Flux;

import java.util.Collection;

public class QueryMethodInterceptorStream<T> implements CollectionTimeStream<T> {

    private final QueryMethodInterceptor interceptor;
    private final JoinPointDelegate point;

    public QueryMethodInterceptorStream(QueryMethodInterceptor interceptor, JoinPointDelegate point) {
        this.interceptor = interceptor;
        this.point = point;
    }

    @Override
    public Flux<T> read(QueryFilter queryFilter) throws Throwable {
        point.replace(0, queryFilter);
        return Flux.fromIterable((Collection<T>) interceptor.invoke(point));
    }
}
