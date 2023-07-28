package cc.allio.uno.data.orm.executor;

import java.util.Map;

/**
 * ResultGroup -> map
 *
 * @author jiangwei
 * @date 2023/4/18 13:24
 * @since 1.1.4
 */
public class MapResultSetHandler implements ResultSetHandler<Map<String, Object>> {

    @Override
    public Map<String, Object> apply(ResultGroup resultGroup) {
        return resultGroup.toMap();
    }
}
