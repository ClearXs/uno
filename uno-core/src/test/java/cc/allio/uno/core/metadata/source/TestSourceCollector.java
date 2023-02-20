package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.metadata.UserMetadata;
import com.google.common.collect.Lists;

import java.util.List;

public class TestSourceCollector implements SourceCollector<UserMetadata> {

    private final List<UserMetadata> cache = Lists.newArrayList();

    @Override
    public void collect(UserMetadata element) {
        cache.add(element);
    }

    public int size() {
        return cache.size();
    }
}
