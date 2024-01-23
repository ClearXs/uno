package cc.allio.uno.data.orm.executor;

/**
 * 不经任何处理
 *
 * @author jiangwei
 * @date 2023/4/18 13:25
 * @since 1.1.4
 */
public class DefaultResultSetHandler implements ResultSetHandler<ResultGroup> {

    @Override
    public ResultGroup apply(ResultGroup resultGroup) {
        return resultGroup;
    }
}
