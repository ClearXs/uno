package cc.allio.uno.data.orm.executor;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Java Bean 转换为 List
 *
 * @author jiangwei
 * @date 2023/4/18 13:30
 * @see BeanResultSetHandler
 * @since 1.1.4
 */
public class ListBeanResultSetHandler<R> implements ListResultSetHandler<R> {

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
}
