package cc.allio.uno.component.flink;

import lombok.Data;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@Data
@ConfigurationProperties("automic.uno.flink")
public class UnoFlinkProperties {

    /**
     * 并行度
     *
     * @see StreamExecutionEnvironment#setParallelism(int)
     */
    private Integer parallelism = 5;

    private final Checkpoint checkpoint = new Checkpoint();

    @Data
    public static class Checkpoint {

        /**
         * 是否开启checkpoint
         */
        private boolean enable = true;

        /**
         * checkpoint周期
         *
         * @see StreamExecutionEnvironment#enableCheckpointing(long)
         */
        private Long checkpointPeriod = 60000L;

        /**
         * 是否强制进行checkpoint
         */
        private boolean forceCheckpointing = true;

        /**
         * checkpoint模式
         *
         * @see CheckpointingMode
         */
        private CheckpointingMode checkpointMode = CheckpointingMode.EXACTLY_ONCE;

        /**
         * checkpoint最小间隔
         */
        private Long minPauseBetweenCheckpoints = 60000L;

        /**
         * checkpoint超时时间
         */
        private Long checkPointTimeout = 180000L;

        /**
         * 允许的并发检查点
         */
        private Integer maxConcurrentCheckpoints = 1;

        /**
         * 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint【详细解释见备注】
         * ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION:表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
         * ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION: 表示一旦Flink处理程序被cancel后，会删除Checkpoint数据，只有job执行失败的时候才会保存checkpoint
         *
         * @see CheckpointConfig.ExternalizedCheckpointCleanup
         */
        private CheckpointConfig.ExternalizedCheckpointCleanup externalizedCheckpointCleanup = CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION;

        /**
         * checkpoint路径
         */
        private String checkpointPath = "file:///Users/jiangwei/Miscellaneous/flink";
    }
}
