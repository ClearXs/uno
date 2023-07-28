package cc.allio.uno.component.kafka;

import cc.allio.uno.core.reactive.BufferRate;
import cc.allio.uno.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义Kafka发送者，包含缓冲区速率控制，给定超时时间
 *
 * @author jiangwei
 * @date 2022/6/23 16:19
 * @since 1.0
 */
@Slf4j
public class UnoKafkaSender {

    private FluxSink<SenderRecord<String, String, String>> transmitter;
    private final KafkaSender<String, String> sender;

    public UnoKafkaSender(Properties producerProperties) {
        this(KafkaSender.create(SenderOptions.create(producerProperties)), BufferRate.DEFAULT_TIMEOUT);
    }

    public UnoKafkaSender(UnoKafkaProperties kafkaProperties) {
        this(KafkaSender.create(SenderOptions.create(kafkaProperties.getProducer().toProperties(kafkaProperties.getBootstraps()))), BufferRate.DEFAULT_TIMEOUT);
    }

    /**
     * 给定缓冲区为100ms缓冲时间
     */
    public UnoKafkaSender(KafkaSender<String, String> sender) {
        this(sender, BufferRate.DEFAULT_TIMEOUT);
    }

    public UnoKafkaSender(KafkaSender<String, String> sender, Duration timeout) {
        this(sender, BufferRate.DEFAULT_SIZE, timeout);
    }


    public UnoKafkaSender(KafkaSender<String, String> sender, int maxSize, Duration timeout) {
        this(sender, BufferRate.DEFAULT_RATE, maxSize, timeout);
    }

    /**
     * 创建kafka Sender实例，基于内部缓冲速率控制
     *
     * @param sender  发送者
     * @param rate    流节点之间处理速率，超过此速率将刷新数据
     * @param maxSize 流里面最大大小
     * @param timeout 超时时间
     */
    public UnoKafkaSender(KafkaSender<String, String> sender, int rate, int maxSize, Duration timeout) {
        this.sender = sender;
        Flux<SenderRecord<String, String, String>> source = Flux.create(sink -> transmitter = sink);
        Flux<SenderRecord<String, String, String>> rateControl = BufferRate.create(source, rate, maxSize, timeout).flatMap(Flux::fromIterable);
        this.sender.send(rateControl)
                .doOnNext(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    if (log.isDebugEnabled()) {
                        log.debug("Message {} sent successfully topic-partition={}-{} offset={} timestamp={}",
                                r.correlationMetadata(),
                                metadata.topic(),
                                metadata.partition(),
                                metadata.offset(),
                                metadata.timestamp());
                    }
                })
                .subscribe();
    }


    /**
     * 发布数据，生产者生产数据
     *
     * @param topic 发布主题，数据第一次汇集
     * @param key   key，在同一主题下，数据分区汇集
     * @param value 数据value
     */
    public void send(String topic, String key, String value) {
        transmitter.next(SenderRecord.create(new ProducerRecord<>(topic, key, value), value));
    }

    /**
     * 生产数据
     *
     * @param topic 主题
     * @param key   消息key
     * @param value 可序列化数据
     * @param <T>   数据的范型
     */
    public <T> void send(String topic, String key, T value) {
        send(topic, key, JsonUtils.toJson(value));
    }

    /**
     * 生产数据
     *
     * @param topic  主题
     * @param values 数据Map结构 key：消息key，value：可序列化数据
     * @param <T>    数据的范型
     */
    public <T> void send(String topic, Map<String, T> values) {
        for (Map.Entry<String, T> keyValue : values.entrySet()) {
            send(topic, keyValue.getKey(), keyValue.getValue());
        }
    }

    /**
     * 事务发送消息，需配置{@link UnoKafkaProperties.Producer#enableIdempotence}与{@link UnoKafkaProperties.Producer#transactionalId}
     *
     * @param topic  主题
     * @param values 数据Map结构 key：消息key，value：可序列化数据
     * @param <T>    数据的范型
     */
    public <T> void txSender(String topic, Map<String, T> values) {
        sender.createOutbound().sendTransactionally(
                        Flux.fromIterable(values.entrySet())
                                .map(entry -> new ProducerRecord<>(topic, entry.getKey(), JsonUtils.toJson(entry.getValue())))
                                .window(10)
                )
                .then()
                .subscribe();
    }

    /**
     * 返回reactive sender对象
     *
     * @return sender实例
     */
    public KafkaSender<String, String> toSender() {
        return this.sender;
    }

}
