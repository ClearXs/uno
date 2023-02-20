package cc.allio.uno.component.flink.task;

import cc.allio.uno.component.flink.Input;
import cc.allio.uno.component.flink.Output;
import cc.allio.uno.core.util.calculate.Calculator;
import org.apache.flink.api.common.io.GenericInputFormat;
import org.apache.flink.api.common.io.InputFormat;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Flink task构建器
 *
 * @param <T> 输入参数范型
 * @param <C> 输出参数范型
 * @author jiangwei
 * @date 2022/2/22 20:37
 * @see AccumulatedEventTimeTask
 * @see AccumulatedCountTask
 * @see CompositionAccumulateEventTimeTask
 * @see #shelves()
 * @since 1.0.4
 */
public class FlinkTaskBuilder<T, C> implements Builder, Serializable {

    transient String name;
    transient TypeInformation<Tuple3<String, Long, T>> inputType;
    transient TypeInformation<C> outputType;
    transient Calculator<C> calculator;
    transient GenericInputFormat<Tuple3<String, Long, T>> input;
    transient SourceFunction<Tuple3<String, Long, T>> source;
    transient SinkFunction<C> sink;

    FlinkTaskBuilder() {

    }

    /**
     * 创建一个任务构建器的空客架子
     *
     * @return 任务构建器实例
     */
    public static <T, C> FlinkTaskBuilder<T, C> shelves() {
        return new FlinkTaskBuilder<>();
    }

    /**
     * 构建任务名称
     *
     * @param name 任务名称
     * @return 当前结果对象
     */
    public FlinkTaskBuilder<T, C> buildName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 构建数据输出sink对象
     *
     * @param sink sink对象实例
     * @return 当前结果对象
     * @see Output
     */
    public FlinkTaskBuilder<T, C> buildSink(SinkFunction<C> sink) {
        this.sink = sink;
        return this;
    }

    /**
     * 构建输入对象，需要实现{@link }
     *
     * @param input 数据对象实例
     * @return 当前对象
     * @see InputFormat
     * @see Input
     */
    public FlinkTaskBuilder<T, C> buildInput(GenericInputFormat<Tuple3<String, Long, T>> input) {
        this.input = input;
        return this;
    }

    /**
     * 构建数据源对象，需要实现{@link Input}接口
     *
     * @param source 数据源对象实例
     * @return 当前构建对象
     * @see SourceFunction
     */
    public FlinkTaskBuilder<T, C> buildSource(SourceFunction<Tuple3<String, Long, T>> source) {
        this.source = source;
        return this;
    }

    /**
     * 构建输入类型实例对象
     *
     * @param inputType {@link TypeInformation}实例对象
     * @return 当前构建器对象
     * @see TypeInformation#of(TypeHint)
     */
    public FlinkTaskBuilder<T, C> buildInputType(TypeInformation<Tuple3<String, Long, T>> inputType) {
        this.inputType = inputType;
        return this;
    }

    /**
     * 构建输出类型实例对象
     *
     * @param outputType {@link TypeInformation}实例对象
     * @return 当前构建器对象
     * @see TypeInformation#of(TypeHint)
     */
    public FlinkTaskBuilder<T, C> buildOutputType(TypeInformation<C> outputType) {
        this.outputType = outputType;
        return this;
    }

    /**
     * 构建计算器实例
     *
     * @param calculator 计算器实例对象
     * @return 当前构造器对象
     */
    public FlinkTaskBuilder<T, C> buildCalculator(Calculator<C> calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * 创建Counter任务构建器
     *
     * @return 返回Counter任务实例
     */
    public CountBuilder<T, C> count() {
        return new CountBuilder<>(this);
    }

    /**
     * 创建event-time任务构建器
     *
     * @return 返回event-time任务
     */
    public EventTimeBuilder<T, C> eventTime() {
        return new EventTimeBuilder<>(this);
    }

    /**
     * 当前构建对象的基准校验
     */
    @Override
    public void benchmarkCheck() {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not null");
        }
    }

    /**
     * 构建普通的Ordinary flink任务
     *
     * @return flink任务实例
     */
    public OrdinaryFlinkTask<T, C> buildOrdinary() {
        benchmarkCheck();
        return new OrdinaryFlinkTask<>(this);
    }

    /**
     * 根据其他分构建器重新构建任务数据
     *
     * @param otherBuilder 其他构建器
     */
    protected void rebuild(FlinkTaskBuilder<T, C> otherBuilder) {
        this.name = otherBuilder.name;
        this.inputType = otherBuilder.inputType;
        this.outputType = otherBuilder.outputType;
        this.calculator = otherBuilder.calculator;
        this.sink = otherBuilder.sink;
        this.input = otherBuilder.input;
        this.source = otherBuilder.source;
    }

    /**
     * Count任务构建器
     *
     * @param <T> 输入参数范型
     * @param <C> 输出参数范型
     */
    public static class CountBuilder<T, C> extends FlinkTaskBuilder<T, C> {
        Integer threshold = 0;
        Integer overlap = 0;

        CountBuilder(FlinkTaskBuilder<T, C> taskBuilder) {
            rebuild(taskBuilder);
        }

        /**
         * 构建计数累积阈值
         *
         * @param threshold 阈值大小，值必须大于0
         * @return builder对象
         */
        public CountBuilder<T, C> buildThreshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }

        /**
         * 构建计数允许的重叠值
         *
         * @param overlap 阈值大小，值必须大于0
         * @return builder对象
         */
        public CountBuilder<T, C> buildOverlap(Integer overlap) {
            this.overlap = overlap;
            return this;
        }

        /**
         * 构建{@link AccumulatedCountTask}对象
         *
         * @return {@link AccumulatedCountTask}对象实例
         */
        public AccumulatedCountTask<T, C> build() {
            benchmarkCheck();
            return new AccumulatedCountTask<>(this);
        }

        @Override
        public void benchmarkCheck() {
            super.benchmarkCheck();
            if (threshold < 0) {
                throw new IllegalArgumentException("threshold must ge 0");
            }
            if (overlap < 0) {
                throw new IllegalArgumentException("overlap must ge 0");
            }
        }
    }

    /**
     * event-time任务构建器
     *
     * @param <T> 输入参数范型
     * @param <C> 输出参数范型
     */
    static class EventTimeBuilder<T, C> extends FlinkTaskBuilder<T, C> {
        transient Time size;
        transient Time slide;
        Long tolerance;

        EventTimeBuilder(FlinkTaskBuilder<T, C> taskBuilder) {
            rebuild(taskBuilder);
        }

        /**
         * 构建event-time窗口大小
         *
         * @param size {@link Time}实例参数
         * @return 当前builder对象
         */
        public EventTimeBuilder<T, C> buildSize(Time size) {
            this.size = size;
            return this;
        }

        /**
         * 构建event-time滑动窗口大小
         *
         * @param slide {@link Time}实例参数
         * @return 当前builder对象
         */
        public EventTimeBuilder<T, C> buildSlide(Time slide) {
            this.slide = slide;
            return this;
        }

        /**
         * 构建watermark容忍度
         *
         * @param tolerance {@link Long}对象实例
         * @return 当前builder对象
         */
        public EventTimeBuilder<T, C> buildTolerance(Long tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        /**
         * 构建输出{@link AccumulatedEventTimeTask}对象
         *
         * @return {@link AccumulatedEventTimeTask}对象实例
         */
        public AccumulatedEventTimeTask<T, C> build() {
            benchmarkCheck();
            return new AccumulatedEventTimeTask<>(this);
        }

        @Override
        public void benchmarkCheck() {
            super.benchmarkCheck();
            if (Objects.isNull(size)) {
                throw new IllegalArgumentException("window size must not null");
            }
            if (Objects.isNull(slide)) {
                throw new IllegalArgumentException("window slide must not null");
            }
        }
    }

    /**
     * 构建组合event-time任务构建器
     *
     * @param <T> 输入参数范型
     * @param <C> 输出参数范型
     */
    static class CompositionEventTimeBuilder<T, C> extends FlinkTaskBuilder<T, C> {

        /**
         * event-times集合对象
         */
        final transient List<Tuple3<Time, Time, Long>> eventTimes;

        CompositionEventTimeBuilder(FlinkTaskBuilder<T, C> taskBuilder) {
            rebuild(taskBuilder);
            eventTimes = new ArrayList<>();
        }

        /**
         * 添加一条event-time事件流
         *
         * @param size      event-time窗口大小
         * @param slide     event-time滑动大小
         * @param tolerance 容忍度
         * @return 当前构建对象
         */
        public CompositionEventTimeBuilder<T, C> append(Time size, Time slide, Long tolerance) {
            eventTimes.add(Tuple3.of(size, slide, tolerance));
            return this;
        }

        /**
         * 构建{@link CompositionAccumulateEventTimeTask}对象
         *
         * @return 返回{@{@link CompositionAccumulateEventTimeTask}实例
         */
        public CompositionAccumulateEventTimeTask<T, C> build() {
            benchmarkCheck();
            return new CompositionAccumulateEventTimeTask<>(this);
        }

        @Override
        public void benchmarkCheck() {
            super.benchmarkCheck();
            if (eventTimes.isEmpty()) {
                throw new IllegalArgumentException("at least one event-time stream");
            }
        }
    }

}
