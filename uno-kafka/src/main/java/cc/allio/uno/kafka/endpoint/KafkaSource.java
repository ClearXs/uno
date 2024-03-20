package cc.allio.uno.kafka.endpoint;

import cc.allio.uno.kafka.UnoKafkaManagement;
import cc.allio.uno.core.metadata.endpoint.source.JsonSource;
import cc.allio.uno.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Kafka数据源
 *
 * @author j.x
 * @date 2022/11/22 10:41
 * @since 1.1.1
 */
@Slf4j
public class KafkaSource extends JsonSource {

    private final String[] topics;

    public KafkaSource(List<String> topics) {
        this(topics.toArray(new String[0]));
    }

    public KafkaSource(String... topics) {
        if (ObjectUtils.isEmpty(topics)) {
            throw new IllegalArgumentException("kafka topics is empty");
        }
        this.topics = topics;
    }

    @Override
    public void register(ApplicationContext context) {
        UnoKafkaManagement kafkaManagement = null;
        try {

            kafkaManagement = context.getBean(UnoKafkaManagement.class);
        } catch (NoSuchBeanDefinitionException ex) {
            log.error("register kafka source failed", ex);
        }
        if (kafkaManagement != null) {
            kafkaManagement.createReceive(topics)
                    .doSubscribe()
                    .parallel(10)
                    .doOnNext(this::next)
                    .subscribe();
        }
    }
}
