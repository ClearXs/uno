package cc.allio.uno.data.orm.executor.result;

import lombok.Setter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 结果集
 *
 * @author j.x
 * @since 1.1.4
 */
@Setter
public class ResultSet implements Iterable<ResultGroup> {

    /**
     * 结果数据
     */
    private List<ResultGroup> resultGroups;

    @Override
    public Iterator<ResultGroup> iterator() {
        if (resultGroups != null) {
            return resultGroups.iterator();
        }
        return Collections.emptyIterator();
    }
}
