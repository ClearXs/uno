package cc.allio.uno.data.query.stream;

import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.util.BeanUtils;
import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.param.DataDilute;
import cc.allio.uno.data.query.param.QuerySetting;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * 数据稀释剂，根据{@link DataDilute}配置项进行数据抽稀，
 *
 * @author jiangwei
 * @date 2022/11/16 11:46
 * @since 1.1.0
 */
public class DiluentTimeStream<T> extends FunctionalityTimeStream<T> {

    public DiluentTimeStream(CollectionTimeStream<T> ds) {
        super(ds);
    }

    @Override
    protected boolean onFilter(QueryFilter queryFilter) {
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        QuerySetting querySetting = queryWrapper.getQuerySetting();
        // 边界条件判断
        return querySetting != null && querySetting.getDataDilute() != null;
    }

    @Override
    protected Flux<T> doRead(QueryFilter queryFilter, Flux<T> source) {
        DiluentList diluent = new DiluentList<>(queryFilter.getQueryWrapper(), this);
        return source.doOnNext(origin -> diluent.add(origin))
                .thenMany(Flux.fromIterable(diluent));
    }

    /**
     * 数据稀释List，其核心于{@link SupplementTimeStream.SupplementList}一致，在进行数据添加时触发执行事件，再对数据进行抽稀。
     * <p>思路是：</p>
     * <ol>
     *     <li>在进行添加数据时，判断当前数据是否包含给定的时间字段<code>timeFiled</code>，如果没有则过滤掉。</li>
     *     <li><b>创建出下一时间点数据（根据时间窗口）</b></li>
     *     <li>如果包含有的话，则判断时间是否在下一个时间点上，如果不再则放入队列之中。</li>
     *     <li>如果在给定的时间窗口中，则触发添加操作，按照给定的队列数据，触发稀释动作。</li>
     * </ol>
     */
    public static class DiluentList<T> extends ArrayDeque<T> {

        /**
         * 查询对象
         */
        private final transient QueryWrapper queryWrapper;

        /**
         * 稀释触发操作
         */
        private final transient DataDilute dataDilute;

        /**
         * 数据时间字段
         */
        private final String timeField;

        /**
         * 时间流
         */
        private final transient TimeStream<T> timeStream;

        /**
         * 不再时间窗口内数据缓存
         */
        private final transient Deque<Object> cache;

        /**
         * 时间点索引指针数据，用于构建时间窗口。
         */
        private transient T timePoint;

        public DiluentList(QueryWrapper queryWrapper, TimeStream<T> timeStream) {
            this.queryWrapper = queryWrapper;
            this.dataDilute = queryWrapper.getQuerySetting().getDataDilute();
            this.timeField = queryWrapper.getTimeField();
            this.timeStream = timeStream;
            this.cache = new ArrayDeque<>();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            boolean modified = false;
            for (T e : c)
                if (add(e))
                    modified = true;
            if (cache.size() != 0) {
                fire();
            }
            return modified;
        }

        @Override
        public boolean add(T o) {
            ValueWrapper wrapper = ValueWrapper.get(o);
            if (Boolean.FALSE.equals(wrapper.contains(timeField))) {
                return false;
            }
            Date currentData = timeStream.dateTime(wrapper.getForce(timeField));
            if (size() == 0) {
                super.add(o);
                reloadTimePoint();
                return true;
            }
            ValueWrapper pointWrapper = ValueWrapper.get(timePoint);
            Date nextTime = timeStream.dateTime(pointWrapper.getForce(timeField));
            long timeSub = Math.abs(nextTime.getTime() - currentData.getTime());
            if (timeSub >= dataDilute.getWindow().getDuration().get().toMillis()) {
                if (cache.size() != 0) {
                    fire();
                }
                super.add(o);
                reloadTimePoint();
            } else {
                cache.offer(o);
            }
            return true;
        }

        /**
         * 触发数据抽稀动作
         */
        public void fire() {
            Object o;
            try {
                Object last = getLast();
                while ((o = cache.poll()) != null) {
                    dataDilute.getAction().getAction().trigger(queryWrapper, last, o);
                }
            } catch (NoSuchElementException ex) {
                // ignore
            }
        }

        /**
         * 重新加载时间点数据，赋值为当前队列最后一个数据
         */
        public void reloadTimePoint() {
            T last = getLast();
            // 赋值下一个时间点数据
            timePoint = (T) BeanUtils.copy(last, last.getClass());
            ValueWrapper pointWrapper = ValueWrapper.get(timePoint);
            Date watermark = timeStream.dateTime(pointWrapper.getForce(timeField));
            timeStream.setTimeData(timeField, pointWrapper, watermark);
        }
    }

}
