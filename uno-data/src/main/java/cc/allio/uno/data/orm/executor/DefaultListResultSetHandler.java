package cc.allio.uno.data.orm.executor;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 返回 list ResultGroup
 *
 * @author jiangwei
 * @date 2023/4/18 13:33
 * @since 1.1.4
 */
public class DefaultListResultSetHandler implements ListResultSetHandler<ResultGroup> {

    @Override
    public List<ResultGroup> apply(ResultSet resultSet) {
        List<ResultGroup> r = Lists.newArrayList();
        for (ResultGroup resultGroup : resultSet) {
            r.add(resultGroup);
        }
        return r;
    }
}
