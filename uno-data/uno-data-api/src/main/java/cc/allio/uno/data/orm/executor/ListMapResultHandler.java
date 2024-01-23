package cc.allio.uno.data.orm.executor;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * list map
 *
 * @author jiangwei
 * @date 2023/4/18 13:34
 * @since 1.1.4
 */
public class ListMapResultHandler implements ListResultSetHandler<Map<String, Object>> {

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
}
