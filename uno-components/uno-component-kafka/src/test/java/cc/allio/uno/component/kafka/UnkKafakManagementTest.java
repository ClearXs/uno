package cc.allio.uno.component.kafka;

import cc.allio.uno.test.BaseTestCase;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class UnkKafakManagementTest extends BaseTestCase {

    @Test
    void testEcho() throws InterruptedException {
        UnoKafkaProperties unoKafkaProperties = new UnoKafkaProperties();
        unoKafkaProperties.setBootstraps(Lists.newArrayList("43.143.195.208:9092"));
        UnoKafkaManagement kafkaManagement = new UnoKafkaManagement(unoKafkaProperties);

        kafkaManagement.createReceive("test")
                .doSubscribe(System.out::println);

        UnoKafkaSender sender = kafkaManagement.createSender(Duration.ofMillis(1L));

        for (int i = 0; i < 10000; i++) {
            sender.send("test", String.valueOf(i), new User(String.valueOf(i)));
        }

        Thread.sleep(1000000L);
    }

    @AllArgsConstructor
    public static class User {
        private String name;
    }
}
