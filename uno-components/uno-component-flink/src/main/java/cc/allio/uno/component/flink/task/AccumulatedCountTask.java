package cc.allio.uno.component.flink.task;

import cc.allio.uno.core.util.calculate.Calculator;
import cc.allio.uno.core.task.AccumulatedTask;
import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.CoreBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.util.Optional;

/**
 * 基于Flink CountSlideWindow数据累计计算
 *
 * @author jiangwei
 * @date 2022/2/17 10:07
 * @see AccumulatedTask
 * @since 1.0
 */
@Slf4j
public class AccumulatedCountTask<T, C> extends AbstractFlinkTask<T, C> {

    private final transient DataStream<C> resultSetStream;

    /**
     * 创建Flink堆积任务
     */
    AccumulatedCountTask(FlinkTaskBuilder.CountBuilder<T, C> builder) {
        super(builder);
        Calculator<C> calculator = builder.calculator;
        resultSetStream = createDataSource()
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<Tuple3<String, Long, T>>noWatermarks()
                                .withTimestampAssigner((v, ctx) -> v.f1))
                .keyBy(t -> t.f0)
                .countWindowAll(builder.threshold, builder.overlap)
                .process(new ProcessAllWindowFunction<Tuple3<String, Long, T>, C, GlobalWindow>() {
                    @Override
                    public void process(ProcessAllWindowFunction<Tuple3<String, Long, T>, C, GlobalWindow>.Context context, Iterable<Tuple3<String, Long, T>> elements, Collector<C> out) throws Exception {
                        C c = calculator.calculation(Optional.ofNullable(CoreBeanUtil.getContext()), CollectionUtils.newArrayList(elements));
                        out.collect(c);
                    }
                }, builder.outputType);
    }

    @Override
    protected DataStream<C> getResultSetStream() {
        return resultSetStream;
    }
}
