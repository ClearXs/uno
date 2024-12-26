package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import reactor.core.publisher.Flux;

import java.util.Date;

/**
 * 排序
 *
 * @author j.x
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
        String timeField = queryWrapper.getTimeField();
        return source
                .filter(o -> !ObjectUtils.isEmpty(o))
                .sort((o1, o2) -> {
                    ValueWrapper o1Wrapper = ValueWrapper.get(o1);
                    ValueWrapper o2Wrapper = ValueWrapper.get(o2);
                    Date o1Date = dateTime(o1Wrapper.getForce(timeField));
                    Date o2Date = dateTime(o2Wrapper.getForce(timeField));
                    return o2Date.compareTo(o1Date);
                });
    }
}
