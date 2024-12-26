package cc.allio.uno.data.query.db.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.db.mapper.QueryMapper;
import cc.allio.uno.data.query.db.query.interceptor.QueryMethodInterceptor;
import cc.allio.uno.core.aop.JoinPointDelegate;
import cc.allio.uno.data.query.stream.CollectionTimeStream;
import reactor.core.publisher.Flux;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 查询数据的入口流，通过{@link JoinPointDelegate}的代理调用，由定义的{@link QueryMapper}获取入口数据流.
 *
 * @author j.x
 * @since 1.1.0
 */
public class MybatisStream<T> implements CollectionTimeStream<T> {

    private final JoinPointDelegate pointDelegate;
    private final QueryMethodInterceptor methodInterceptor;

    public MybatisStream(QueryMethodInterceptor methodInterceptor, JoinPointDelegate pointDelegate) {
        this.pointDelegate = pointDelegate;
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Flux<T> read(QueryFilter queryFilter) throws Throwable {
        Method invokeMethod = methodInterceptor.getMethod();
        Object queryResult = methodInterceptor.getMethod().invoke(pointDelegate.getTarget(), queryFilter);
        Class<?> returnType = invokeMethod.getReturnType();
        return Flux.fromIterable((Collection<T>) returnType.cast(queryResult));
    }
}
