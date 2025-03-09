package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.result.ResultGroup;
import cc.allio.uno.data.orm.executor.result.ResultSet;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * list map
 *
 * @author j.x
 * @since 1.1.4
 */
public class ListMapResultHandler extends ExecutorOptionsAwareImpl implements ListResultSetHandler<Map<String, Object>> {

    private final MapResultSetHandler handler;

    public ListMapResultHandler() {
        this.handler = new MapResultSetHandler();
    }

    @Override
    public List<Map<String, Object>> apply(ResultSet resultSet) {
        List<Map<String, Object>> r = Lists.newArrayList();
        for (ResultGroup resultGroup : resultSet) {
            r.add(handler.apply(resultGroup));
        }
        return r;
    }

    @Override
    public Class<Map<String, Object>> getResultType() {
        return handler.getResultType();
    }
}
