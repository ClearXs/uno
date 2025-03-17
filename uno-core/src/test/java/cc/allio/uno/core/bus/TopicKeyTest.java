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

    @Test
    void testAppendEmpty() {
        String p1 = TopicKey.of("cc").append(b -> b.text("ttt--asd").pathway(Pathway.EMPTY)).getPath();
        assertEquals("/cc/ttt--asd", p1);

        String p2 = TopicKey.of("cc")
                .append(b -> b.text("ttt--asd").pathway(Pathway.EMPTY))
                .append(b -> b.text("ttt--asd").pathway(Pathway.EMPTY))
                .getPath();

        assertEquals("/cc/ttt--asd/ttt--asd", p2);
    }

    @AllArgsConstructor
    @Data
    public static class Student {
        private String code;
        private String name;
    }
}
