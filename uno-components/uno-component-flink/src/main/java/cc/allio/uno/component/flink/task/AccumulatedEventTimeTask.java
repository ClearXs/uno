package cc.allio.uno.component.flink.task;

import cc.allio.uno.core.util.calculate.Calculator;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.CoreBeanUtil;
import lombok.Data;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Optional;

/**
 * 基于Flink TimeSlide时间窗口统计任务
 *
 * @author jiangwei
 * @date 2022/2/22 16:10
 * @since 1.0
 */
public class AccumulatedEventTimeTask<T, C> extends AbstractFlinkTask<T, C> {

    private final transient DataStream<C> resultSetStream;

    /**
     * 创建基于event-time的窗口累积任务实例
     *
     * @see Calculator
     * @see Time
     */
    AccumulatedEventTimeTask(FlinkTaskBuilder.EventTimeBuilder<T, C> builder) {
        super(builder);
        resultSetStream = createDataSource()
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Tuple3<String, Long, T>>forBoundedOutOfOrderness(Duration.ofMillis(builder.tolerance))
                                .withTimestampAssigner((v, ctx) -> v.f1))
                .keyBy(v -> v.f0)
                .window(SlidingEventTimeWindows.of(builder.size, builder.slide))
                .process(new ProcessWindowFunction<Tuple3<String, Long, T>, C, String, TimeWindow>() {
                    @Override
                    public void process(String s, ProcessWindowFunction<Tuple3<String, Long, T>, C, String, TimeWindow>.Context context, Iterable<Tuple3<String, Long, T>> elements, Collector<C> out) throws Exception {
                        C expect = builder.calculator.calculation(Optional.ofNullable(CoreBeanUtil.getContext()), CollectionUtils.newArrayList(elements));
                        out.collect(expect);
                    }
                }, builder.outputType);
    }

    @Override
    protected DataStream<C> getResultSetStream() {
        return resultSetStream;
    }

    @Data
    static class CountTimeout<T> {
        private String key;
        private Long lastTimestamp;
        private T data;
    }

    static class TimeoutProcessFunction<T> extends KeyedProcessFunction<String, Tuple3<String, Long, T>, Tuple3<String, Long, T>> {

        private ValueState<CountTimeout> valueState;
        private final Long timeout;

        TimeoutProcessFunction(Long timeout) {
            this.timeout = timeout;
        }

        @Override
        public void open(Configuration parameters) throws Exception {
            valueState = getRuntimeContext().getState(new ValueStateDescriptor<>("state", CountTimeout.class));
        }

        @Override
        public void processElement(Tuple3<String, Long, T> value, KeyedProcessFunction<String, Tuple3<String, Long, T>, Tuple3<String, Long, T>>.Context ctx, Collector<Tuple3<String, Long, T>> out) throws Exception {
            CountTimeout countTimeout = valueState.value();
            // 处理每一个元素
            if (countTimeout == null) {
                countTimeout = new CountTimeout();
                countTimeout.setKey(value.f0);
                countTimeout.setData(value.f2);
            }
            countTimeout.lastTimestamp = ctx.timestamp();
            valueState.update(countTimeout);
            ctx.timerService().registerEventTimeTimer(countTimeout.lastTimestamp + timeout);
        }

        @Override
        public void onTimer(long timestamp, KeyedProcessFunction<String, Tuple3<String, Long, T>, Tuple3<String, Long, T>>.OnTimerContext ctx, Collector<Tuple3<String, Long, T>> out) throws Exception {
            CountTimeout<T> value = valueState.value();
            if (value.lastTimestamp + timeout == timestamp) {
                out.collect(Tuple3.of(value.key, value.lastTimestamp, value.data));
                valueState.update(null);
            }
        }
    }
}
