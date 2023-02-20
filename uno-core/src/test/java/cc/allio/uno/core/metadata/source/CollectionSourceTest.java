package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.metadata.endpoint.source.CollectionSource;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CollectionSourceTest extends Assertions {

    /**
     * Test Case: 测试数据源数据是否正确
     */
    @Test
    void testNext() {
        CollectionSource source = new CollectionSource(Lists.newArrayList("1", "2"));
        source.subscribe(json -> {
            String jsonStr = json.toString();
            assertEquals("[\"1\",\"2\"]", jsonStr);
        });
        source.register(null);
    }

}
