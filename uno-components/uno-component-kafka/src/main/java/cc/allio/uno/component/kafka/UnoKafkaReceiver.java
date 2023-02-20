package cc.allio.uno.component.kafka;

import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 封装{@link KafkaReceiver}
 *
 * @author jiangwei
 * @date 2022/6/23 16:38
 * @since 1.0
 */
@Slf4j
public class UnoKafkaReceiver {

    private final KafkaReceiver<String, String> receiver;

    public UnoKafkaReceiver(UnoKafkaProperties kafkaProperties) {
        this(kafkaProperties,
                kafkaProperties.getTopic()
                        .entrySet()
                        .stream()
                        .flatMap(entry -> {
                            List<String> value = entry.getValue();
                            return Stream.of(value.toArray(new String[]{}));
                        })
                        .collect(Collectors.toList())
                        .toArray(new String[]{}));
    }

    public UnoKafkaReceiver(UnoKafkaProperties kafkaProperties, String[] topics) {
        Properties consumerProperties = kafkaProperties.getConsumer().toProperties(kafkaProperties.getBootstraps());
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.create(consumerProperties);
        receiverOptions.subscription(Arrays.asList(topics));
        this.receiver = KafkaReceiver.create(receiverOptions);
    }


    /**
     * 订阅某个消息队列
     *
     * @param consumer 消息消费者
     */
    public void doSubscribe(Consumer<String> consumer) {
        receiver
                .receive()
                .doOnNext(record -> {
                    ReceiverOffset receiverOffset = record.receiverOffset();
                    String value = record.value();
                    log.info("Thread {} Kafka Received message: topic-partition={}-{} offset={} timestamp={} key={} value={}",
                            Thread.currentThread().getName(),
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.timestamp(),
                            record.key(),
                            record.value());
                    consumer.accept(value);
                    receiverOffset.acknowledge();
                })
                .subscribe();
    }

    public void batchSubscribe(int bufferSize, Consumer<List<String>> consumer) {
        receiver
                .receive()
                .buffer(bufferSize)
                .doOnNext(batchs -> {
                    log.info("Thread {} Kafka Received message Size {}", Thread.currentThread().getName(), batchs.size());
                    consumer.accept(batchs.stream().map(ReceiverRecord::value).collect(Collectors.toList()));
                    for (ReceiverRecord<String, String> record : batchs) {
                        ReceiverOffset receiverOffset = record.receiverOffset();
                        receiverOffset.acknowledge();
                    }
                })
                .subscribe();
    }

    /**
     * 返回Reactive Receiver实例对象
     *
     * @return Receiver实例对象
     */
    public KafkaReceiver<String, String> receiver() {
        return receiver;
    }

}
