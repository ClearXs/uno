package cc.allio.uno.component.flink.task;

import cc.allio.uno.component.flink.*;
import cc.allio.uno.core.task.Task;
import cc.allio.uno.core.util.CoreBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.api.common.io.GenericInputFormat;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import cc.allio.uno.core.util.calculate.Calculator;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Flink Task任务
 *
 * @author jiangwei
 * @date 2022/2/21 13:47
 * @since 1.0
 */
@Slf4j
public abstract class AbstractFlinkTask<T, C> implements Task, Serializable {

    private transient StreamExecutionEnvironment env;
    private final transient String taskName;
    private final transient SinkFunction<C> sink;
    private final transient SourceFunction<Tuple3<String, Long, T>> datasource;
    private final transient GenericInputFormat<Tuple3<String, Long, T>> input;
    private final transient TypeInformation<Tuple3<String, Long, T>> inputType;
    private transient JobClient jobClient;
    private transient ExecutorService exector = Executors.newSingleThreadExecutor();

    protected AbstractFlinkTask(FlinkTaskBuilder<T, C> taskBuilder) {
        this.taskName = taskBuilder.name;
        this.sink = taskBuilder.sink;
        this.datasource = taskBuilder.source;
        this.input = taskBuilder.input;
        this.inputType = taskBuilder.inputType;
    }

    /**
     * 创建本地流执行环境
     *
     * @return 环境执行对象
     */
    public synchronized StreamExecutionEnvironment getEnv() {
        if (env == null) {
            UnoFlinkProperties flinkProperties = CoreBeanUtil.getBeanOrDefault(UnoFlinkProperties.class, new UnoFlinkProperties());
            env = FlinkEnvBuilder.skeleton()
                    .buildEnvName(taskName)
                    .builderEnvProperties(flinkProperties)
                    .build();
        }
        return env;
    }

    /**
     * 以三元组为数据源类型创建数据源对象
     *
     * @return 数据源对象实例
     */
    protected DataStreamSource<Tuple3<String, Long, T>> createDataSource() {
        if (datasource != null) {
            return getEnv().addSource(datasource, inputType);
        }
        return getEnv().createInput(input, inputType);
    }

    @Override
    public void run() throws Exception {
        exector.submit(() -> {
            try {
                // 添加数据sink
                if (getResultSetStream() != null) {
                    getResultSetStream().addSink(sink);
                }
                jobClient = getEnv().executeAsync(taskName);
                CompletableFuture<JobStatus> jobStatus = jobClient.getJobStatus();
                JobStatus status = jobStatus.get();
                if (status.isGloballyTerminalState()) {
                    finish();
                }
            } catch (Exception e) {
                log.error("Running job failed", e);
            }
        });
    }

    @Override
    public void finish() {
        if (jobClient != null) {
            jobClient.cancel();
        }
    }

    // --------------------- abstract ---------------------

    /**
     * 获取触发{@link Calculator}计算后的结果集数据流对象，用作：
     * <ol>
     *     <li>执行{@link Calculator#calculation(Optional, Object)}之后添加转换操作</li>
     *     <li>添加checkpoint</li>
     *     <li>添加sink</li>
     * </ol>
     * 执行{{@link #run()}}时将会添加一个未来的结果集
     *
     * @return 输入流数据对象实例
     */
    protected abstract DataStream<C> getResultSetStream();


}
