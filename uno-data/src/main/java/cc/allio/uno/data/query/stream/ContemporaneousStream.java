package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.mybatis.query.stream.MybatisStream;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.param.DateDimension;
import cc.allio.uno.core.util.ObjectUtil;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * 数据同期对比数据流。
 * <p>接受一个入口流{@link MybatisStream}，根据同期对比的参数数据重新构建入口流的{@link QueryFilter}参数。根据该新参数获取集合流数据，把集合流数据转换为以时间为key的参数</p>
 *
 * @author jiangwei
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
        if (ObjectUtil.isEmpty(contemporaneous)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(contemporaneous)
                .map(con -> {
                    Date time = con.getDimension().getToDate().apply(con.getDate());
                    Date startTime = con.getDimension().getTimeStart().apply(time);
                    Date endTime = con.getDimension().getTimeEnd().apply(time);
                    QueryFilter newFilter = queryWrapper.build();
                    newFilter.whereSql().between(queryWrapper.getTimeField(), startTime, endTime);
                    try {
                        List<?> result = stream.read(newFilter).collectList().block();
                        // key 时间 value 数据
                        Map<String, Collection<?>> dateDimensionEntity = Maps.newHashMap();
                        dateDimensionEntity.put(con.getDate(), result);
                        return dateDimensionEntity;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce(Maps.newHashMap(), (o, n) -> {
                    o.putAll(n);
                    return o;
                });
    }
}
