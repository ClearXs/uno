package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultGroup;

/**
 * Java Bean对象处理器
 *
 * @author j.x
 * @since 1.1.4
 */
public class BeanResultSetHandler<R> extends ExecutorOptionsAwareImpl implements ResultSetHandler<R>, BeanResultHandler<R> {

    private final Class<R> beanClass;

    public BeanResultSetHandler(Class<R> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public R apply(ResultGroup resultGroup) {
        return resultGroup.toEntity(beanClass);
    }

    @Override
    public Class<R> getBeanType() {
        return beanClass;
    }

    @Override
    public Class<R> getResultType() {
        return getBeanType();
    }
}
