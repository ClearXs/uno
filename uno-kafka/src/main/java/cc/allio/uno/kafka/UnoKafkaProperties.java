package cc.allio.uno.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Kafka配置参数<a href="https://kafka.apache.org/documentation/#configuration">configuration</a>
 *
 * @author jiangwei
 * @date 2022/2/25 09:29
 * @since 1.0
 */
@Data
@ConfigurationProperties("allio.uno.kafka")
public class UnoKafkaProperties {

    /**
     * 是否启用kafka
     */
    private boolean enable = false;

    /**
     * broker地址
     */
    private List<String> bootstraps = Collections.singletonList("localhost:9092");

    /**
     * Additional properties, common to producers and consumers, used to configure the client.
     */
    private final Map<String, String> properties = new HashMap<>();

    private final Map<String, List<String>> topic = new HashMap<>();

    private final Consumer consumer = new Consumer();

    private final Producer producer = new Producer();

    @Data
    public static class Consumer {

        /**
         * consumer 配置参数
         */
        private final Map<String, String> properties = new HashMap<>();

        /**
         * group.id
         */
        private String groupId = "uno-group";

        /**
         * 指定位移消费：
         * <ul>
         *     <li>latest：默认，从最新位置开始消费（如果在_consumer_offset没有找到）</li>
         *     <li>earliest：从0开始消费（如果在_consumer_offset没有找到）</li>
         *     <li>none：直接抛出异常</li>
         * </ul>
         */
        private String autoOffsetReset = "earliest";

        /**
         * 是否自动提交消费进度，由封装程序进行自动提交，避免重复消费（但可能存在消费丢失的可能性）。
         */
        private Boolean enableAutoCommit = true;

        /**
         * 自动提交消费进度频率
         */
        private Long autoCommitIntervalMs = 1000L;

        /**
         * 单次调用poll()返回的最大记录数
         */
        private Integer maxPollRecords = 500;

        /**
         * 发出请求时传递给服务器的id字符串。这样做的目的是通过允许逻辑应用程序名称包含在服务器端请求日志记录中，能够跟踪仅ip/端口之外的请求源。
         */
        private String clientId = "test";

        /**
         * 处理这些消息所用时间不能超过该值，即：两次poll的时间间隔最大时间
         */
        private Long maxPollIntervalMs = 300000L;

        /**
         * 消息的 key 的反序列化方式
         */
        private String keyDeserializer = StringDeserializer.class.getName();

        /**
         * 消息的 value 的反序列化方式
         */
        private String valueDeserializer = StringDeserializer.class.getName();

        /**
         * 隔离界别 read_uncommitted、read_committed
         */
        private String isolationLevel = "read_uncommitted";

        /**
         * @see ConsumerConfig#HEARTBEAT_INTERVAL_MS_CONFIG
         */
        private Long heartbeatIntervalMs = 3000L;

        /**
         * 把当前所有配置参数以{@link Properties}形式存储，其中包含broker地址
         *
         * @return 返回{@{@link Properties}实例
         */
        public Properties toProperties(List<String> bootstraps) {
            Properties combine = new Properties();
            combine.putAll(toMap(bootstraps));
            return combine;
        }

        /**
         * 把当前所有配置参数以{@link Map}形式存储，其中包含broker地址
         *
         * @return 返回{@{@link Map}配置实例对象
         */
        public Map<String, String> toMap(List<String> bootstraps) {
            HashMap<String, String> reProperties = new HashMap<>(properties);
            reProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstraps));
            reProperties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
            reProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
            reProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(this.autoCommitIntervalMs));
            reProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(this.enableAutoCommit));
            reProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, String.valueOf(this.keyDeserializer));
            reProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, String.valueOf(this.valueDeserializer));
            reProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, this.clientId);
            reProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, String.valueOf(this.maxPollRecords));
            reProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, String.valueOf(this.maxPollIntervalMs));
            reProperties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, String.valueOf(this.isolationLevel));
            return reProperties;
        }

    }

    @Data
    public static class Producer {

        /**
         * producer 配置参数
         */
        private final Map<String, String> properties = new HashMap<>();

        /**
         * 0-不应答。1-leader 应答。all-所有 leader 和 follower 应答。
         */
        private String acks = "1";

        /**
         * 发送失败时，重试发送的次数
         */
        private String retries = "3";

        /**
         * 消息的 key 的序列化方式
         */
        private String keySerializer = StringSerializer.class.getName();

        /**
         * 消息的 value 的序列化方式
         */
        private String valueSerializer = StringSerializer.class.getName();

        /**
         * 分批发送 每次批量发送消息的最大数量 默认4M
         */
        private String batchSize = "4194304";

        /**
         * 每次批量发送消息的最大内存 单位 字节  默认 32M
         */
        private String bufferMemory = "33554432";

        /**
         * 批处理延迟时间上限。达到这个延迟后，无法消息是否达到最大数量都会发送，默认25ms
         */
        private String lingerMs = "100";

        /**
         * 生产者事务ID
         */
        private String transactionalId = "";

        /**
         * 是否开启幂等性
         */
        private String enableIdempotence = "false";

        /**
         * 生产者消息压缩类型，gzip、snappy、lz4、zstd
         */
        private String compressionType = "";


        /**
         * 把当前所有配置参数以{@link Properties}形式存储，其中包含broker地址
         *
         * @return 返回{@{@link Properties}实例
         */
        public Properties toProperties(List<String> bootstraps) {
            Properties combine = new Properties();
            combine.putAll(toMap(bootstraps));
            return combine;
        }

        /**
         * 把当前所有配置参数以{@link Map}形式存储，其中包含broker地址
         *
         * @return 返回{@{@link Map}配置实例对象
         */
        public Map<String, String> toMap(List<String> bootstraps) {
            HashMap<String, String> reProperties = new HashMap<>(this.properties);
            reProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", bootstraps));
            reProperties.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(retries));
            reProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
            reProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
            reProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
            reProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
            reProperties.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
            if (StringUtils.hasText(transactionalId)) {
                reProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
            }
            reProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);
            if (StringUtils.hasText(compressionType)) {
                reProperties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
            }
            return reProperties;
        }
    }
}
