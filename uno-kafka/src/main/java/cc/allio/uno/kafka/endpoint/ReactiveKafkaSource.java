package cc.allio.uno.kafka.endpoint;

import cc.allio.uno.kafka.UnoKafkaManagement;
import cc.allio.uno.kafka.UnoKafkaReceiver;
import cc.allio.uno.core.metadata.endpoint.source.reactive.ReactiveSource;
import cc.allio.uno.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * reactive kafka source
 *
 * @author j.x
 * @date 2023/4/27 18:09
 * @since 1.1.4
 */
@Slf4j
public class ReactiveKafkaSource implements ReactiveSource<String> {

    private final String[] topics;

    private UnoKafkaReceiver receiver;

    public ReactiveKafkaSource(List<String> topics) {
        this(topics.toArray(new String[0]));
    }

    public ReactiveKafkaSource(String... topics) {
        if (ObjectUtils.isEmpty(topics)) {
            throw new IllegalArgumentException("kafka topics is empty");
        }
        this.topics = topics;
    }

    @Override
    public Flux<String> subscribe() {
        if (receiver == null) {
            throw new NullPointerException("kafka receiver is empty, ensure invoke register() method");
        }
        return receiver.receiver()
                .receive()
                .map(r -> {
                    log.info("Thread {} Kafka Received message: topic-partition={}-{} offset={} timestamp={} key={} value={}",
                            Thread.currentThread().getName(),
                            r.topic(),
                            r.partition(),
                            r.offset(),
                            r.timestamp(),
                            r.key(),
                            r.value());
                    r.receiverOffset().acknowledge();
                    return r.value();
                })
                .onErrorContinue((err, o) -> log.error("kafka receive object {} has err", o.toString(), err));
    }

    @Override
    public void register(ApplicationContext context) {
        UnoKafkaManagement kafkaManagement = null;
        try {
            kafkaManagement = context.getBean(UnoKafkaManagement.class);
            this.receiver = kafkaManagement.createReceive(topics);
        } catch (NoSuchBeanDefinitionException | NullPointerException ex) {
            log.error("register kafka source failed", ex);
        }
    }
}
