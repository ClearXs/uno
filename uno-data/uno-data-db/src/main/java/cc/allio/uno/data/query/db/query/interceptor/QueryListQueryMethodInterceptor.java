package cc.allio.uno.data.query.db.query.interceptor;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.db.mapper.QueryMapper;
import cc.allio.uno.data.query.db.query.stream.MybatisStream;
import cc.allio.uno.data.query.stream.StreamBuilder;
import cc.allio.uno.core.aop.JoinPointDelegate;

import java.lang.reflect.Method;

/**
 * {@link QueryMapper#queryList(QueryFilter) }方法拦截器
 *
 * @author j.x
 * @since 1.1.0
 */
final class QueryListQueryMethodInterceptor implements QueryMethodInterceptor {
    static final Method QUERY_LIST;

    static {
        try {
            QUERY_LIST = QueryMapper.class.getMethod("queryList", QueryFilter.class);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object invoke(JoinPointDelegate point) throws Throwable {
        // 源数据项 -> 排序 -> 增补 -> 过滤 -> 抽稀
        return new StreamBuilder<>(new MybatisStream<>(this, point))
                .sort()
                .supplement()
                .outliersIgnore()
                .diluent()
                .buildAsync()
                .read(point.getArg(QueryFilter.class));
    }

    @Override
    public Method getMethod() {
        return QUERY_LIST;
    }
}
