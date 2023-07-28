package cc.allio.uno.data.orm.executor;

import lombok.Setter;

import java.util.Iterator;
import java.util.List;

/**
 * 结果集
 *
 * @author jiangwei
 * @date 2023/5/28 23:11
 * @since 1.1.4
 */
public class ResultSet implements Iterable<ResultGroup> {

    /**
     * 结果数据
     */
    @Setter
    private List<ResultGroup> resultGroups;

    @Override
    public Iterator<ResultGroup> iterator() {
        if (resultGroups != null) {
            return resultGroups.iterator();
        }
        throw new NullPointerException("resultGroups for list is empty");
    }
}
