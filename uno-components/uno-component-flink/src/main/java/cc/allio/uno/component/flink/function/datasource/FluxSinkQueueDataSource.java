package cc.allio.uno.component.flink.function.datasource;

import cc.allio.uno.component.flink.Input;
import cc.allio.uno.component.flink.function.BaseCountDownRichSourceFunction;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.Serializable;
import java.util.Queue;

/**
 * 基于{@link Flux}创建的外部数据源，通过调用{@link FluxSink#next(Object)}把数据向下游输出</br>
 * 下游输出数据放入unbounded Queue（{@link MpscUnboundedArrayQueue}），触发{{@link #run(SourceFunction.SourceContext)}}时从队列中拿取数据。</br>
 * 除此之外，它还实现了{@link CheckpointedFunction}接口，来对数据进行checkpoint
 *
 * @author jiangwei
 * @date 2022/2/23 13:29
 * @since 1.0
 */
@Deprecated
public class FluxSinkQueueDataSource<T>
        extends BaseCountDownRichSourceFunction<T>
        implements CheckpointedFunction, Input<T>, Serializable {

    private volatile boolean isRunning = true;

    /**
     * 无界队列
     */
    private static Queue emitter;

    /**
     * 数据流上游，由外部输入数据
     */
    private static FluxSink upstream;

    /**
     * checkpoint状态数据
     */
    private transient ListState<T> snapshotStateData;

    private final TypeInformation<T> inputType;

    public FluxSinkQueueDataSource(TypeInformation<T> inputType) {
        this.inputType = inputType;
    }

    @Override
    public void afterOpen(Configuration parameters) {
        Flux<T> emit = Flux.create(sink -> upstream = sink);
        emitter = PlatformDependent.newMpscQueue();
        emit.subscribe(v -> emitter.offer(v));
    }

    @Override
    public void input(T data) {
        if (upstream != null) {
            upstream.next(data);
        }
    }

    @Override
    public void run(SourceContext<T> ctx) throws Exception {
        while (isRunning) {
            synchronized (ctx.getCheckpointLock()) {
                if (!emitter.isEmpty()) {
                    ctx.collect((T) emitter.poll());
                }
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }

    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        snapshotStateData.clear();
        for (Object data : emitter) {
            snapshotStateData.add((T) data);
        }
    }

    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        ListStateDescriptor<T> descriptor = new ListStateDescriptor<>("FluxSinkQueueDataSource", inputType);
        snapshotStateData = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()) {
            for (T data : snapshotStateData.get()) {
                emitter.add(data);
            }
        }
    }
}
