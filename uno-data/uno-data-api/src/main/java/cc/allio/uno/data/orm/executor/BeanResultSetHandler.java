package cc.allio.uno.data.orm.executor;

/**
 * Java Bean对象处理器
 *
 * @author jiangwei
 * @date 2023/4/18 13:17
 * @since 1.1.4
 */
public class BeanResultSetHandler<R> implements ResultSetHandler<R> {

    private final Class<R> beanClass;

    public BeanResultSetHandler(Class<R> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public R apply(ResultGroup resultGroup) {
        return resultGroup.toEntity(beanClass);
    }
}
