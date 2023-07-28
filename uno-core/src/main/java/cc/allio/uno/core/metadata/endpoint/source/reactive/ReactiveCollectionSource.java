package cc.allio.uno.core.metadata.endpoint.source.reactive;


import org.springframework.context.ApplicationContext;

import java.util.Collection;

public class ReactiveCollectionSource<C> extends ReactiveSinkSource<C> {

    private final Collection<C> sources;

    public ReactiveCollectionSource(Collection<C> sources) {
        super();
        this.sources = sources;
    }

    @Override
    public void register(ApplicationContext context) {
        sources.forEach(this::next);
    }
}
