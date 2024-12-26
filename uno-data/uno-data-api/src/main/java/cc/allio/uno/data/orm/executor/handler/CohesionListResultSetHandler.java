package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultGroup;
import cc.allio.uno.data.orm.executor.ResultSet;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * decorate {@link ListBeanResultSetHandler} from {@link ResultSetHandler}
 *
 * @author j.x
 * @since 0.1.1
 */
public class CohesionListResultSetHandler<R> implements ListResultSetHandler<R> {

    private final ResultSetHandler<R> resultSetHandler;

    public CohesionListResultSetHandler(ResultSetHandler<R> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
    }

    @Override
    public Class<R> getResultType() {
        return resultSetHandler.getResultType();
    }

    @Override
    public List<R> apply(ResultSet resultGroups) {
        List<R> results = Lists.newArrayList();
        for (ResultGroup resultGroup : resultGroups) {
            R r = resultSetHandler.apply(resultGroup);
            results.add(r);
        }
        return results;
    }
}
