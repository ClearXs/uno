package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.metadata.UserMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SourceCollectorTest extends Assertions {

    @Test
    void testCollector() {
        TestSourceCollector collector = new TestSourceCollector();
        collector.collect(new UserMetadata());
        assertEquals(1, collector.size());
    }

}
