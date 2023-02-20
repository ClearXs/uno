package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Async stream
 *
 * @author jiangwei
 * @date 2023/1/20 12:05
 * @since 1.1.3
 */
public class AsyncStream<T> implements DataStream<Collection<T>> {

    CollectionTimeStream<T> source;

    public AsyncStream(CollectionTimeStream<T> source) {
        this.source = source;
    }

    @Override
    public Collection<T> read(QueryFilter queryFilter) throws Throwable {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        return source.read(queryFilter).collectList().block();
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .exceptionally(err -> Collections.emptyList())
                .get(5000, TimeUnit.MINUTES);
    }
}
