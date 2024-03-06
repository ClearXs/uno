package cc.allio.uno.kafka;

import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.stream.Stream;

public class UnoKafkaTest extends BaseTestCase {

    UnoKafkaReceiver unoKafkaReceiver;
    UnoKafkaSender sender;
    private final String topic = "test-0703";

    public void init() {
        UnoKafkaProperties kafkaProperties = new UnoKafkaProperties();
        kafkaProperties.setBootstraps(Lists.newArrayList("43.143.195.208:9092"));
        unoKafkaReceiver = new UnoKafkaReceiver(kafkaProperties, Stream.of(topic).toArray(String[]::new));
        sender = new UnoKafkaSender(kafkaProperties);
    }

    @Test
    void testConsumer() throws InterruptedException {
        init();
        User user = new User();
        user.setName("123");

        sender.send(topic, "test", JsonUtils.toJson(user));

        unoKafkaReceiver.doSubscribe()
                .subscribe(System.out::println);

        Thread.sleep(60000L);
    }


    @Test
    void testSenderPerformance() throws InterruptedException {
        init();
        for (int i = 0; i < 100000; i++) {
            User user = new User();
            user.setId(i);
            user.setName("123");
            sender.send(topic, "test", JsonUtils.toJson(user));
        }
        Thread.sleep(60000L);
    }

    @Test
    void testReceiverPerformance() throws InterruptedException {
        init();
        unoKafkaReceiver.doSubscribe()
                .subscribe(System.out::println);
        Thread.sleep(60000L);
    }

    @Test
    void testConsumerPosition() {
        init();
        TopicPartition topicPartition = new TopicPartition(topic, 0);
        unoKafkaReceiver.receiver()
                .doOnConsumer(stringStringConsumer -> {
                    System.out.println(stringStringConsumer);
                    return stringStringConsumer;
                })
                .subscribe();
        unoKafkaReceiver.doSubscribe()
                .as(StepVerifier::create)
                .expectTimeout(Duration.ofMillis(1000L))
                .verify();
        unoKafkaReceiver.position(topicPartition)
                .as(StepVerifier::create)
                .expectTimeout(Duration.ofSeconds(100))
                .verify();
    }


    @Data
    static class User {
        private int id;
        private String name;
        private int age;
    }
}