package cc.allio.uno.data.query.stream;

import reactor.core.publisher.Flux;

/**
 * 集合数据流
 *
 * @author j.x
 * @since 1.1.0
 */
public interface CollectionTimeStream<T> extends TimeStream<Flux<T>> {

}
