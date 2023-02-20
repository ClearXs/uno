package cc.allio.uno.component.flink.task;

import cc.allio.uno.core.util.Collections;
import cc.allio.uno.core.util.CoreBeanUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 公用一个{@link StreamExecutionEnvironment}，通过不同的窗口大小区分不同的流式计算任务。通过多流合并达到结果
 *
 * @author jiangwei
 * @date 2022/2/22 18:28
 * @since 1.0
 */
public class CompositionAccumulateEventTimeTask<T, C> extends AbstractFlinkTask<T, C> {

    private final transient DataStream<C> resultSetStream;

    public CompositionAccumulateEventTimeTask(FlinkTaskBuilder.CompositionEventTimeBuilder<T, C> builder) {
        super(builder);
        List<Tuple3<Time, Time, Long>> eventTimes = builder.eventTimes;
        List<SingleOutputStreamOperator<C>> unMergeStream = eventTimes.stream()
                .map(t3 ->
                        createDataSource()
                                .assignTimestampsAndWatermarks(
                                        WatermarkStrategy
                                                .<Tuple3<String, Long, T>>forBoundedOutOfOrderness(Duration.ofMillis(t3.f2))
                                                .withTimestampAssigner((v, ctx) -> v.f1))
                                .keyBy(v -> v.f0)
                                .window(SlidingEventTimeWindows.of(t3.f0, t3.f1))
                                .process(new ProcessWindowFunction<Tuple3<String, Long, T>, C, String, TimeWindow>() {
                                    @Override
                                    public void process(String s, ProcessWindowFunction<Tuple3<String, Long, T>, C, String, TimeWindow>.Context context, Iterable<Tuple3<String, Long, T>> elements, Collector<C> out) throws Exception {
                                        C expect = builder.calculator.calculation(Optional.ofNullable(CoreBeanUtil.getContext()), Collections.newArrayList(elements));
                                        out.collect(expect);
                                    }
                                }, builder.outputType))
                .collect(Collectors.toList());
        SingleOutputStreamOperator<C> mergeStream = unMergeStream.get(0);
        unMergeStream = unMergeStream.subList(1, unMergeStream.size());
        resultSetStream = mergeStream.union(unMergeStream.toArray(new DataStream[]{}));
    }

    @Override
    protected DataStream<C> getResultSetStream() {
        return resultSetStream;
    }
}
