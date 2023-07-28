package cc.allio.uno.core.metadata.source;

import cc.allio.uno.core.BaseTestCase;
import cc.allio.uno.core.metadata.endpoint.source.reactive.DefaultReactiveAggregationSource;
import cc.allio.uno.core.metadata.endpoint.source.reactive.ReactiveCollectionSource;
import cc.allio.uno.core.metadata.endpoint.source.reactive.ReactiveObjectSource;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

public class ReactiveSourceTest extends BaseTestCase {

    @Test
    void testCollection() {
        ReactiveCollectionSource<Integer> source = new ReactiveCollectionSource<>(Lists.newArrayList(1, 2));
        source.subscribe()
                .doOnNext(System.out::println)
                .subscribe();
        source.register(null);
    }

    @Test
    void testAggregation() {
        DefaultReactiveAggregationSource<Integer> sources = new DefaultReactiveAggregationSource<>(Integer.class);
        sources.registerSource(new ReactiveCollectionSource<>(Lists.newArrayList(1, 2)));
        ReactiveObjectSource<Object> oSource = new ReactiveObjectSource<>();
        sources.registerSource(oSource);
        sources.subscribe()
                .doOnNext(System.out::println)
                .subscribe();

        oSource.next("asd");

        sources.register(null);


        sources.next(1);
    }
}
