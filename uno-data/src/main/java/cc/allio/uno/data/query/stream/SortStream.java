package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.ObjectUtil;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.sql.Condition;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.query.OrderCondition;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * 排序
 *
 * @author jiangwei
 * @date 2023/1/20 10:56
 * @since 1.1.4
 */
public class SortStream<T> extends FunctionalityTimeStream<T> {

    public SortStream(CollectionTimeStream<T> ds) {
        super(ds);
    }

    @Override
    protected boolean onFilter(QueryFilter queryFilter) {
        return true;
    }

    @Override
    protected Flux<T> doRead(QueryFilter queryFilter, Flux<T> source) {
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        RuntimeColumn timeColumn = queryFilter.getOrderDelegate()
                .getColumns()
                .stream()
                .collect(Collectors.toMap(RuntimeColumn::getName, order -> order))
                .get(queryWrapper.getTimeField());
        String timeField = queryWrapper.getTimeField();
        return source
                .filter(o -> !ObjectUtil.isEmpty(o))
                .sort((o1, o2) -> {
                    ObjectWrapper o1Wrapper = new ObjectWrapper(o1);
                    ObjectWrapper o2Wrapper = new ObjectWrapper(o2);
                    Date o1Date = dateTime(o1Wrapper.getForce(timeField));
                    Date o2Date = dateTime(o2Wrapper.getForce(timeField));
                    // 没有包含则进行排序，默认按照降序
                    if (timeColumn == null) {
                        return o2Date.compareTo(o1Date);
                    }
                    Condition condition = timeColumn.getCondition();
                    if (OrderCondition.DESC == condition) {
                        return o2Date.compareTo(o1Date);
                    } else if (OrderCondition.ASC == condition) {
                        return o1Date.compareTo(o2Date);
                    } else {
                        return 0;
                    }
                });
    }
}
