package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.QueryWrapper;
import cc.allio.uno.data.query.param.QuerySetting;
import cc.allio.uno.data.query.param.Window;
import cc.allio.uno.data.sql.RuntimeColumn;
import cc.allio.uno.data.sql.query.OrderCondition;
import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.util.CoreBeanUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 在原始数据中，数据会出现'截断'的情况，其原因是这部分数据可能并没有存在。
 * <p>在该功能性数据流中把'截断'的数据进行修复，进行数据增补</p>
 * <p><b>在进行增补之前数据需要按照{@link QueryWrapper#getTimeField()}进行排序，否则将会出现无限循环</b></p>
 * <p>数据增补算法：</p>
 * <ol>
 *     <li>从返回数据中（不一定会排序，程序在一遍进行排序，需要判断）</li>
 *     <li>取抽稀时间如果抽稀没有时间的话则取默认1H</li>
 *     <li>循环判断两个数据之间的差值是否大于默认1H数据，如果大于，则在取在下一个数据自此判断是否大于2H。如果都大于则进行数据增补</li>
 *     <li>增补时首先计算增补点数据量，并对数据增补</li>
 *     <li>增补时间依赖于数据排序规则，在参数{@link QueryFilter#getOrderDelegate()} ()}中没有指定排序规则，则默认按照{@link OrderCondition#DESC}进行</li>
 * </ol>
 *
 * @author jiangwei
 * @date 2022/11/16 13:20
 * @see SupplementList
 * @since 1.1.0
 */
public class SupplementTimeStream<T> extends FunctionalityTimeStream<T> {

    public SupplementTimeStream(CollectionTimeStream<T> ds) {
        super(ds);
    }

    @Override
    protected boolean onFilter(QueryFilter queryFilter) {
        QueryWrapper queryWrapper = queryFilter.getQueryWrapper();
        QuerySetting querySetting = queryWrapper.getQuerySetting();
        return querySetting != null && querySetting.isSupplement();
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
        return source.collectList()
                .flatMapMany(origin -> {
                    // 取时间间距
                    long timeSpacing = timeSpacing(queryWrapper, origin);
                    SupplementList supplementList = new SupplementList(timeSpacing, timeField, timeColumn, queryWrapper.getDataFields(), this);
                    supplementList.addAll(origin);
                    return Flux.fromIterable(supplementList);
                });
    }

    /**
     * 获取数据增补的时间间隔。best match
     * <p>尝试从给定排好序的数据中提取出数据可能的间隔，如果间隔是异常的，那么判断是否有数据抽稀的时间窗口，如果没有则取1H</p>
     *
     * @param queryWrapper 查询领域对象
     * @param origin       原始数据
     * @return 时间间隔
     */
    public long timeSpacing(QueryWrapper queryWrapper, Collection<T> origin) {
        String timeField = queryWrapper.getTimeField();
        // 抽取数据
        Sampling sampling = new Sampling(origin, 2);
        // 取样本数
        int sampleSize = 3;
        long timeSpacing = Flux.range(0, sampleSize)
                .flatMap(count ->
                        Flux.fromIterable(sampling.getNext())
                                .window(2, 1)
                                .flatMap(compare ->
                                        compare.collectList()
                                                .filter(maybeCoplue -> maybeCoplue.size() > 1)
                                                .flatMap(couple -> {
                                                    Object o1 = couple.get(0);
                                                    Object o2 = couple.get(1);
                                                    ObjectWrapper o1Wrapper = new ObjectWrapper(o1);
                                                    ObjectWrapper o2Wrapper = new ObjectWrapper(o2);
                                                    Date o1Date = dateTime(o1Wrapper.getForce(timeField));
                                                    Date o2Date = dateTime(o2Wrapper.getForce(timeField));
                                                    return Mono.just(Math.abs(o1Date.getTime() - o2Date.getTime()));
                                                }))
                                .switchIfEmpty(Mono.just(0L)))
                .collectList()
                .flatMap(sample -> Mono.just(sample.stream().min(Long::compare).orElse(0L)))
                .block();

        long paramTimeSpacing;
        if (queryWrapper.getQuerySetting() != null && queryWrapper.getQuerySetting().getDataDilute() != null) {
            paramTimeSpacing = queryWrapper.getQuerySetting().getDataDilute().getWindow().getDuration().get().toMillis();
        } else {
            paramTimeSpacing = Window.ONE_HOUR.getDuration().get().toMillis();
        }
        // 增补最小时间间隔为5min
        return timeSpacing < Duration.ofMinutes(5).toMillis() ? paramTimeSpacing : Math.min(timeSpacing, paramTimeSpacing);
    }

    /**
     * 按照时间做增量数据列表，其思路在于增加时对数据进行判断。<b>数据需要按照时间排序</b>
     * <ul>
     *     <li>如果给定的数据中没有时间字段，则不进行增加。</li>
     *     <li>使用两个字段<code>timeSpacing</code>标识<u>时间间隔</u><code>watermark</code>标识<u>当前最后一个数据时刻。</u></li>
     *     <li>当数据新增时，使用<code>watermark</code>与当前数据比较<code>timeSpacing</code>的差值，如果小于则放行，否则增补数据，直到小于。</li>
     * </ul>
     */
    public static class SupplementList<T> extends ArrayList<T> {

        /**
         * 时间间隔
         */
        private final long timeSpacing;

        /**
         * 排序字段
         */
        private final transient RuntimeColumn timeColumn;

        /**
         * 时间字段
         */
        private final transient String timeField;

        /**
         * 增量数据设置数据字段
         */
        private final String[] dataFields;

        /**
         * 时间流
         */
        private final transient TimeStream<T> timeStream;

        /**
         * 记录当前数据的时刻，
         */
        private Date watermark;

        public SupplementList(long timeSpacing, String timeField, RuntimeColumn timeColumn, String[] dataFields, TimeStream<T> timeStream) {
            this.timeSpacing = timeSpacing;
            this.timeField = timeField;
            this.timeColumn = timeColumn;
            this.dataFields = dataFields;
            this.timeStream = timeStream;
        }

        @Override
        public boolean add(T o) {
            ObjectWrapper wrapper = new ObjectWrapper(o);
            if (Boolean.FALSE.equals(wrapper.contains(timeField))) {
                return false;
            }
            Date currentData = timeStream.dateTime(wrapper.getForce(timeField));
            if (size() == 0) {
                watermark = currentData;
                return super.add(o);
            }
            while (Math.abs(watermark.getTime() - currentData.getTime()) > timeSpacing) {
                watermark = timeStream.nextStepDate(watermark, timeSpacing, timeColumn == null ? OrderCondition.DESC : (OrderCondition) timeColumn.getCondition());
                T increment = (T) CoreBeanUtil.copy(o, o.getClass());
                ObjectWrapper incrementWrapper = new ObjectWrapper(increment);
                // 设置时间
                timeStream.setTimeData(timeField, incrementWrapper, watermark);
                // 设置数据
                timeStream.setDataFieldsData(dataFields, incrementWrapper);
                super.add(increment);
            }
            watermark = currentData;
            return super.add(o);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            boolean modified = false;
            for (T e : c)
                if (e != null && add(e))
                    modified = true;
            return modified;
        }
    }
}
