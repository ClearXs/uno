package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import cc.allio.uno.data.query.exception.QueryException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Async stream
 *
 * @author j.x
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
                        AtomicReference<List<T>> ref = new AtomicReference<>();
                        source.read(queryFilter).collectList().subscribe(ref::set);
                        return ref.get();
                    } catch (Throwable ex) {
                        throw new QueryException(ex);
                    }
                })
                .exceptionally(err -> Collections.emptyList())
                .get(5000, TimeUnit.MINUTES);
    }
}
