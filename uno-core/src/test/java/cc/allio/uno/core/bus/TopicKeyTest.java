package cc.allio.uno.core.bus;

import cc.allio.uno.core.BaseTestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class TopicKeyTest extends BaseTestCase {

    @Test
    void testCreateStudentTopicKey() {
        TopicKey topicKey = TopicKey.of("/test", new Student("2", "3"));
        assertEquals("/test/2/3", topicKey.getPath());
    }

    @Test
    void testAppend() {
        String path = TopicKey.of("cc").append("allio").getPath();
        assertEquals("/cc/allio", path);
    }


    @AllArgsConstructor
    @Data
    public static class Student {
        private String code;
        private String name;
    }
}
