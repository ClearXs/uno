package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class TopicKeyTest extends BaseTestCase {

    @Test
    void testCreateStudentTopicKey() {
        TopicKey topicKey = TopicKey.create("/test", new Student("2", "3"));
        assertEquals("/test/2/3", topicKey.getPath());
    }


    @AllArgsConstructor
    @Data
    public static class Student {
        private String code;
        private String name;
    }
}
