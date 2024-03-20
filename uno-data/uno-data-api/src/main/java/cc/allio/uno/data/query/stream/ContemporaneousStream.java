package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.ObjectUtils;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.exception.QueryException;
import cc.allio.uno.data.query.param.DateDimension;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 数据同期对比数据流。
 * <p>接受一个入口流{@link DataStream}，根据同期对比的参数数据重新构建入口流的{@link QueryFilter}参数。根据该新参数获取集合流数据，把集合流数据转换为以时间为key的参数</p>
 *
 * @author j.x
 * @date 2022/11/18 14:40
 * @since 1.1.0
 */
public class ContemporaneousStream implements TimeStream<Map<String, Collection<?>>> {

    private final CollectionTimeStream<?> stream;

    public ContemporaneousStream(CollectionTimeStream<?> stream) {
        this.stream = stream;
    }

    @Override
    public Map<String, Collection<?>> read(QueryFilter queryFilter) throws Throwable {
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        DateDimension[] contemporaneous = queryWrapper.getContemporaneous();
        if (ObjectUtils.isEmpty(contemporaneous)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(queryWrapper.groupByDateDimension())
                .map(gc -> {
                    List<?> re = Collections.emptyList();
                    try {
                        AtomicReference<List<?>> ref = new AtomicReference<>();
                        stream.read(gc.getT2()).collectList().subscribe(ref::set);
                        List<?> result = ref.get();
                        if (CollectionUtils.isNotEmpty(result)) {
                            re = result;
                        }
                    } catch (Throwable ex) {
                        throw new QueryException(ex);
                    }
                    // key 时间 value 数据
                    Map<String, Collection<?>> dateDimensionEntity = Maps.newHashMap();
                    dateDimensionEntity.put(gc.getT1().getDate(), re);
                    return dateDimensionEntity;
                })
                .reduce(Maps.newHashMap(), (o, n) -> {
                    o.putAll(n);
                    return o;
                });
    }
}
