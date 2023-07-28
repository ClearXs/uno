package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.data.query.mybatis.mapper.QueryMapper;
import cc.allio.uno.data.query.mybatis.QueryFilter;
import cc.allio.uno.data.query.mybatis.QueryWrapper;
import com.google.common.collect.Maps;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 指定某一个集合数据流，按照给定的数据字段（{@link QueryWrapper#getDataFields()}）使其转换为{@link ValueTime}的时间数据。
 *
 * @author jiangwei
 * @date 2022/11/18 14:33
 * @since 1.1.0
 */
public class ValueTimeStream implements TimeStream<Map<String, Collection<ValueTime>>> {

    private final CollectionTimeStream<?> stream;

    public ValueTimeStream(CollectionTimeStream<?> stream) {
        this.stream = stream;
    }

    /**
     * 列表实体数据转换为时间数据，如某个对象实体 包含test、time字段。则返回只有包含这个字段.
     * 主用于{@link QueryMapper#queryList(QueryFilter)}参数转换
     *
     * @param queryFilter 查询过滤器
     * @return 转换为当前结构：test:[{"time":"xxxx","value":"test"}] ...
     * @see QueryMapper#queryList(QueryFilter)
     */
    @Override
    public Map<String, Collection<ValueTime>> read(QueryFilter queryFilter) throws Throwable {
        Flux<?> origin = stream.read(queryFilter);
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        String[] dataFields = queryWrapper.getDataFields();
        String timeField = queryWrapper.getTimeField();
        AtomicReference<Map<String, Collection<ValueTime>>> ref = new AtomicReference<>();
        Flux.fromArray(dataFields)
                .flatMap(dataField ->
                        origin.map(o -> {
                            ValueWrapper wrapper = ValueWrapper.get(o);
                            Object maybeTime = wrapper.getForce(timeField);
                            Date dateTime = dateTime(maybeTime);
                            Object value = wrapper.getForce(dataField);
                            return Tuples.of(dataField, new ValueTime(dateTime, value));
                        })
                )
                .groupBy(Tuple2::getT1)
                .flatMap(g ->
                        g.map(Tuple2::getT2)
                                .collectList()
                                .map(valueTimes -> Tuples.of(Objects.requireNonNull(g.key()), valueTimes)))
                .reduce(Maps.<String, Collection<ValueTime>>newHashMap(), (m, vt) -> {
                    m.put(vt.getT1(), vt.getT2());
                    return m;
                })
                .subscribe(ref::set);
        return ref.get();
    }
}
