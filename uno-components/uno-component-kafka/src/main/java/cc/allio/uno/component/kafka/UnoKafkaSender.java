package cc.allio.uno.component.kafka;

import cc.allio.uno.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.util.Map;

/**
 * 自定义Kafka发送者
 *
 * @author jiangwei
 * @date 2022/6/23 16:19
 * @since 1.0
 */
@Slf4j
public class UnoKafkaSender {

    private FluxSink<SenderRecord<String, String, String>> transmitter;

    private final KafkaSender<String, String> sender;

    /**
     * 默认延迟1s
     *
     * @param sender 发送
     */
    public UnoKafkaSender(KafkaSender<String, String> sender) {
        this(sender, Duration.ofMillis(1000L));
    }

    public UnoKafkaSender(KafkaSender<String, String> sender, Duration duration) {
        this.sender = sender;
        this.sender.send(Flux.<SenderRecord<String, String, String>>create(sink -> transmitter = sink))
                .delayElements(duration)
                .doOnNext(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    log.info("Message {} sent successfully topic-partition={}-{} offset={} timestamp={}",
                            r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            metadata.timestamp());
                })
                .subscribe();
    }


    /**
     * 发布数据
     *
     * @param topic 发布主题
     * @param key   key
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
        send(topic, key, JsonUtil.toJson(value));
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
                                .map(entry -> new ProducerRecord<>(topic, entry.getKey(), JsonUtil.toJson(entry.getValue())))
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
