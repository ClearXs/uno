package cc.allio.uno.data.query.stream;

import reactor.core.publisher.Flux;

/**
 * 集合数据流
 *
 * @author jiangwei
 * @date 2022/11/18 13:06
 * @since 1.1.0
 */
public interface CollectionTimeStream<T> extends TimeStream<Flux<T>> {

}
