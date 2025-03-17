package cc.allio.uno.core.path;

import cc.allio.uno.core.BaseTestCase;
import org.junit.jupiter.api.Test;

public class ForestTest extends BaseTestCase {

    Forest<String> root;

    @Override
    protected void onInit() throws Throwable {
        root = Forest.createRoot();
    }

    @Test
    void createPath() {
        root.append("/test/child1");

        assertTrue(root.getTopic("/test").isPresent());
        assertTrue(root.getTopic("/test/child1").isPresent());

        assertFalse(root.getTopic("/test/child2").isPresent());
    }

    @Test
    void testRemove() {
        root.append("/test/child1");

        assertTrue(root.getTopic("/test/child1").isPresent());
        assertTrue(root.getTopic("/test").isPresent());

    }
}
