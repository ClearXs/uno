package cc.allio.uno.component.kafka;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Kafka管理器
 *
 * @author jiangwei
 * @date 2022/6/23 16:42
 * @since 1.0
 */
public class UnoKafkaManagement {

    private final UnoKafkaProperties kafkaProperties;

    public UnoKafkaManagement(UnoKafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    public UnoKafkaSender createSender() {
        Properties producerProperties = kafkaProperties.getProducer().toProperties(kafkaProperties.getBootstraps());
        return new UnoKafkaSender(KafkaSender.create(SenderOptions.create(producerProperties)));
    }

    public UnoKafkaSender createSender(Duration duration) {
        Properties producerProperties = kafkaProperties.getProducer().toProperties(kafkaProperties.getBootstraps());
        return new UnoKafkaSender(KafkaSender.create(SenderOptions.create(producerProperties)), duration);
    }

    public UnoKafkaReceiver createReceive(String... topics) {
        return new UnoKafkaReceiver(kafkaProperties, topics);
    }

    /**
     * 根据可以获取配置的topic
     *
     * @param key
     * @return
     */
    public List<String> getTopic(String key) {
        return kafkaProperties.getTopic().getOrDefault(key, Collections.emptyList());
    }
}
