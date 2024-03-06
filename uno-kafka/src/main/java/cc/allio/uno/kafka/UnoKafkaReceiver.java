package cc.allio.uno.kafka;

import cc.allio.uno.core.reactive.BufferRate;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
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

    /**
     * flux receiver
     */
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
                    log.debug("Thread {} Kafka Received message: topic-partition={}-{} offset={} timestamp={} key={} value={}",
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
     * 批量消息
     *
     * @return
     */
    public Flux<String> doSubscribe() {
        return doSubscribe(BufferRate.DEFAULT_SIZE);
    }

    /**
     * 批量消息
     *
     * @param bufferSize
     * @return
     */
    public Flux<String> doSubscribe(int bufferSize) {
        Flux<ReceiverRecord<String, String>> source = receiver.receive();
        Flux<List<ReceiverRecord<String, String>>> buffer = BufferRate.create(source, BufferRate.DEFAULT_RATE, bufferSize, BufferRate.DEFAULT_TIMEOUT);
        return buffer.flatMap(Flux::fromIterable)
                .flatMap(MonoOffset::new);
    }

    /**
     * {@link org.apache.kafka.clients.consumer.Consumer#assignment()}
     *
     * @return The setValue of partitions currently assigned to this consumer
     */
    public Flux<TopicPartition> assignment() {
        return receiver.doOnConsumer(org.apache.kafka.clients.consumer.Consumer::assignment)
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * {@link org.apache.kafka.clients.consumer.Consumer#position(TopicPartition)}
     *
     * @param topicPartition the topic partition
     * @return
     */
    public Mono<Long> position(TopicPartition topicPartition) {
        return receiver.doOnConsumer(consumer -> consumer.position(topicPartition));
    }

    /**
     * {@link org.apache.kafka.clients.consumer.Consumer#position(TopicPartition, Duration)}
     *
     * @param topicPartition the topic partition
     * @param timeout        the timeout
     * @return
     */
    public Mono<Long> position(TopicPartition topicPartition, Duration timeout) {
        return receiver.doOnConsumer(consumer -> consumer.position(topicPartition, timeout));
    }

    /**
     * 返回Reactive Receiver实例对象
     *
     * @return Receiver实例对象
     */
    public KafkaReceiver<String, String> receiver() {
        return receiver;
    }

    /**
     * 位移提交
     */
    static class MonoOffset extends Mono<String> {

        private final ReceiverRecord<String, String> record;

        public MonoOffset(ReceiverRecord<String, String> record) {
            this.record = record;
        }

        @Override
        public void subscribe(CoreSubscriber<? super String> actual) {
            String value = record.value();
            if (log.isDebugEnabled()) {
                log.debug("Thread {} Kafka Received message: topic-partition={}-{} offset={} timestamp={} key={} value={}",
                        Thread.currentThread().getName(),
                        record.topic(),
                        record.partition(),
                        record.offset(),
                        record.timestamp(),
                        record.key(),
                        record.value());
            }
            actual.onSubscribe(Operators.scalarSubscription(actual, value));
        }

    }
}
