package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultGroup;

import java.util.Collections;
import java.util.Map;

/**
 * ResultGroup -> map
 *
 * @author j.x
 * @since 1.1.4
 */
public class MapResultSetHandler extends ExecutorOptionsAwareImpl implements ResultSetHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> apply(ResultGroup resultGroup) {
        return resultGroup.toMap();
    }

    @Override
    public Class<Map<String, Object>> getResultType() {
        return (Class<Map<String, Object>>) Collections.EMPTY_MAP.getClass();
    }
}
