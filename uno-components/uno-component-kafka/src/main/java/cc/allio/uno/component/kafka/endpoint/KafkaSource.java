package cc.allio.uno.component.kafka.endpoint;

import cc.allio.uno.component.kafka.UnoKafkaManagement;
import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * Kafka数据源
 *
 * @author jiangwei
 * @date 2022/11/22 10:41
 * @since 1.1.1
 */
@Slf4j
public class KafkaSource extends JsonSource {

    private final String[] topics;

    public KafkaSource(String... topics) {
        this.topics = topics;
    }

    @Override
    public void register(ApplicationContext context) {
        UnoKafkaManagement kafkaManagement = context.getBean(UnoKafkaManagement.class);
        kafkaManagement.createReceive(topics)
                .receiver()
                .receive()
                .parallel(10)
                .doOnNext(record -> {
                    log.info("Thread {} Kafka Received message: topic-partition={}-{} offset={} timestamp={} key={} value={}",
                            Thread.currentThread().getName(),
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.timestamp(),
                            record.key(),
                            record.value());
                    next(record.value());
                    record.receiverOffset().acknowledge();
                })
                .subscribe();
    }
}
