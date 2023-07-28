package cc.allio.uno.data.orm.repository;

import com.google.common.collect.Maps;
import org.reactivestreams.Publisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.core.EntityInformation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

/**
 * Reactive Local Repository
 *
 * @author jiangwei
 * @date 2022/12/27 13:54
 * @since 1.1.4
 */
public abstract class ReactiveLocalRepository<T, ID extends Serializable> extends ApplicationRepository implements R2dbcRepository<T, ID> {

    private final Map<ID, T> local = Maps.newConcurrentMap();
    private final EntityInformation<T, ID> metadata;

    protected ReactiveLocalRepository(EntityInformation<T, ID> metadata) {
        this.metadata = metadata;
    }

    @Override
    public Flux<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Mono<S> save(S entity) {
        ID id = metadata.getId(entity);
        local.put(id, entity);
        return Mono.just(entity);
    }

    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> entities) {
        return Flux.fromIterable(entities)
                .doOnNext(entity -> {
                    ID id = metadata.getId(entity);
                    local.put(id, entity);
                });
    }

    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> entityStream) {
        return Flux.from(entityStream)
                .doOnNext(entity -> {
                    ID id = metadata.getId(entity);
                    local.put(id, entity);
                });
    }

    @Override
    public Mono<T> findById(ID id) {
        return Mono.justOrEmpty(local.get(id));
    }

    @Override
    public Mono<T> findById(Publisher<ID> id) {
        return Mono.from(id)
                .map(local::get);
    }

    @Override
    public Mono<Boolean> existsById(ID id) {
        return Mono.just(local.containsKey(id));
    }

    @Override
    public Mono<Boolean> existsById(Publisher<ID> id) {
        return Mono.just(id)
                .map(local::containsKey);
    }

    @Override
    public Flux<T> findAll() {
        return Flux.fromIterable(local.values());
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> ids) {
        return Flux.fromIterable(ids)
                .map(local::get);
    }

    @Override
    public Flux<T> findAllById(Publisher<ID> idStream) {
        return Flux.from(idStream)
                .map(local::get);
    }

    @Override
    public Mono<Long> count() {
        return Mono.just((long) local.size());
    }

    @Override
    public Mono<Void> deleteById(ID id) {
        return deleteById(Mono.just(id));
    }

    @Override
    public Mono<Void> deleteById(Publisher<ID> id) {
        return Mono.from(id)
                .doOnNext(local::remove)
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> delete(T entity) {
        ID id = metadata.getId(entity);
        local.remove(id);
        return Mono.empty();
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends T> entities) {
        return Flux.fromIterable(entities)
                .doOnNext(entity -> {
                    ID id = metadata.getId(entity);
                    local.remove(id);
                })
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {
        return Flux.from(entityStream)
                .doOnNext(entity -> {
                    ID id = metadata.getId(entity);
                    local.remove(id);
                })
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> deleteAll() {
        local.clear();
        return Mono.empty();
    }
}
