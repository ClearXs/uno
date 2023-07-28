package cc.allio.uno.core.metadata.endpoint;

import cc.allio.uno.core.User;
import cc.allio.uno.core.metadata.UserMetadata;
import cc.allio.uno.core.metadata.endpoint.source.CollectionSource;
import cc.allio.uno.core.metadata.source.TestSourceCollector;
import cc.allio.uno.core.metadata.source.TestSourceConverter;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultEndpointTest extends Assertions {

    @Test
    void testCollect() {

        DefaultEndpoint<UserMetadata> endpoint = new DefaultEndpoint<>();
        TestSourceConverter converter = new TestSourceConverter();
        endpoint.setConverter(converter);
        TestSourceCollector collector = new TestSourceCollector();
        endpoint.setCollector(collector);

        // 设置数据源
        endpoint.registerSource(new CollectionSource(
                Lists.newArrayList(new User("id", "name", ""))));

        try {
            endpoint.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, collector.size());
    }

}
