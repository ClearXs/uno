package cc.allio.uno.component.flink.task;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;

/**
 * 没有实现function，对外提供{@link DataStreamSource}的Flink任务
 *
 * @author jiangwei
 * @date 2022/2/24 23:23
 * @since 1.0
 */
public class OrdinaryFlinkTask<IN, OUT> extends AbstractFlinkTask<IN, OUT> {

    OrdinaryFlinkTask(FlinkTaskBuilder<IN, OUT> taskBuilder) {
        super(taskBuilder);
    }

    @Override
    @Deprecated
    protected DataStream<OUT> getResultSetStream() {
        return null;
    }
}
