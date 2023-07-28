package cc.allio.uno.component.flink.task;

import cc.allio.uno.component.flink.UnoFlinkProperties;
import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.ObjectUtils;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * flink环境构建
 *
 * @author jiangwei
 * @date 2022/3/1 15:28
 * @since 1.0.5
 */
public class FlinkEnvBuilder implements Builder, Serializable {

    /**
     * env名称
     */
    transient String name;

    /**
     * flink配置属性
     */
    transient UnoFlinkProperties flinkProperties;

    FlinkEnvBuilder() {

    }

    /**
     * 创建环境构建的骨架
     *
     * @return 环境构造器实例对象
     */
    public static FlinkEnvBuilder skeleton() {
        return new FlinkEnvBuilder();
    }

    public FlinkEnvBuilder buildEnvName(String name) {
        this.name = name;
        return this;
    }

    public FlinkEnvBuilder builderEnvProperties(UnoFlinkProperties flinkProperties) {
        this.flinkProperties = flinkProperties;
        return this;
    }

    /**
     * 构建flink env对象
     *
     * @return env对象实例
     */
    public StreamExecutionEnvironment build() {
        benchmarkCheck();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(flinkProperties.getParallelism());
        env.getConfig().disableClosureCleaner();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        boolean enableCheckpoint = flinkProperties.getCheckpoint().isEnable();
        if (enableCheckpoint) {
            // 每隔1000 ms进行启动一个检查点【设置checkpoint的周期】
            env.enableCheckpointing(flinkProperties.getCheckpoint().getCheckpointPeriod());
            // 高级选项：
            // 设置模式为exactly-once （这是默认值）
            env.getCheckpointConfig().setCheckpointingMode(flinkProperties.getCheckpoint().getCheckpointMode());
            // 检查是否强制未对齐的检查点，尽管当前存在不可检查点的迭代反馈或自定义分区。
            env.getCheckpointConfig().setForceUnalignedCheckpoints(flinkProperties.getCheckpoint().isForceCheckpointing());
            // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
            env.getCheckpointConfig().setMinPauseBetweenCheckpoints(flinkProperties.getCheckpoint().getMinPauseBetweenCheckpoints());
            // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
            env.getCheckpointConfig().setCheckpointTimeout(flinkProperties.getCheckpoint().getCheckPointTimeout());
            // 同一时间只允许进行一个检查点
            env.getCheckpointConfig().setMaxConcurrentCheckpoints(flinkProperties.getCheckpoint().getMaxConcurrentCheckpoints());
            // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】
            //ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION:表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
            //ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: 表示一旦Flink处理程序被cancel后，会删除Checkpoint数据，只有job执行失败的时候才会保存checkpoint
            env.getCheckpointConfig().setExternalizedCheckpointCleanup(flinkProperties.getCheckpoint().getExternalizedCheckpointCleanup());
            //设置statebackend
            env.getCheckpointConfig().setCheckpointStorage(flinkProperties
                    .getCheckpoint()
                    .getCheckpointPath()
                    .concat(StringPool.SLASH)
                    .concat(name));
        }
        return env;
    }

    @Override
    public void benchmarkCheck() {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not null");
        }
        if (ObjectUtils.isEmpty(flinkProperties)) {
            throw new IllegalArgumentException("flink properties must not null");
        }
    }
}
