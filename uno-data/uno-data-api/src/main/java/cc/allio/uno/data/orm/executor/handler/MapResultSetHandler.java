package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultGroup;

import java.util.Map;

/**
 * ResultGroup -> map
 *
 * @author j.x
 * @date 2023/4/18 13:24
 * @since 1.1.4
 */
public class MapResultSetHandler extends ExecutorOptionsAwareImpl implements ResultSetHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> apply(ResultGroup resultGroup) {
        return resultGroup.toMap();
    }
}
