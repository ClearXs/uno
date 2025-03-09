package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultSet;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Java Bean 转换为 List
 *
 * @author j.x
 * @see BeanResultSetHandler
 * @since 1.1.4
 */
public class ListBeanResultSetHandler<R> extends ExecutorOptionsAwareImpl implements ListResultSetHandler<R>, ListBeanResultHandler<R> {

    private final BeanResultSetHandler<R> handler;

    public ListBeanResultSetHandler(Class<R> beanClass) {
        this.handler = new BeanResultSetHandler<>(beanClass);
    }

    @Override
    public List<R> apply(ResultSet resultSet) {
        List<R> r = Lists.newArrayList();
        for (ResultGroup next : resultSet) {
            r.add(handler.apply(next));
        }
        return r;
    }

    @Override
    public Class<R> getBeanType() {
        return handler.getBeanType();
    }

    @Override
    public Class<R> getResultType() {
        return handler.getBeanType();
    }
}
