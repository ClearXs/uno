package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.result.ResultGroup;

/**
 * 不经任何处理
 *
 * @author j.x
 * @since 1.1.4
 */
public class DefaultResultSetHandler extends ExecutorOptionsAwareImpl implements ResultSetHandler<ResultGroup> {

    @Override
    public ResultGroup apply(ResultGroup resultGroup) {
        return resultGroup;
    }

    @Override
    public Class<ResultGroup> getResultType() {
        return ResultGroup.class;
    }
}
