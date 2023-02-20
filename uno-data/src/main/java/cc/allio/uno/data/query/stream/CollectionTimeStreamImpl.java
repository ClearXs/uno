package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import reactor.core.publisher.Flux;

/**
 * 读取某个集合数据流
 *
 * @author jiangwei
 * @date 2022/11/18 14:28
 * @since 1.1.0
 */
public class CollectionTimeStreamImpl<T> implements CollectionTimeStream<T> {

    private final Flux<T> c;

    public CollectionTimeStreamImpl(Flux<T> c) {
        this.c = c;
    }

    @Override
    public Flux<T> read(QueryFilter queryFilter) throws Throwable {
        return c;
    }
}
