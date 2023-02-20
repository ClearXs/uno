package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;

import java.util.Collection;

public class ReadStream<T> implements DataStream<Collection<T>> {
    CollectionTimeStream<T> source;

    public ReadStream(CollectionTimeStream<T> source) {
        this.source = source;
    }

    @Override
    public Collection<T> read(QueryFilter queryFilter) throws Throwable {
        return source.read(queryFilter).collectList().block();
    }
}
