package cc.allio.uno.data.repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 默认本地存储
 *
 * @author jiangwei
 * @date 2022/11/24 01:38
 * @since 1.1.2
 */
public abstract class LocalRepository<T, ID extends Serializable> extends ApplicationRepository implements PagingAndSortingRepository<T, ID> {

    private final Map<ID, T> local = Maps.newConcurrentMap();
    private final EntityInformation<T, ID> metadata;

    protected LocalRepository(EntityInformation<T, ID> metadata) {
        this.metadata = metadata;
    }

    @Override
    public <S extends T> S save(S entity) {
        ID id = metadata.getId(entity);
        local.put(id, entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return entities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(local.get(id));
    }

    @Override
    public boolean existsById(ID id) {
        return local.containsKey(id);
    }

    @Override
    public Iterable<T> findAll() {
        return local.values();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return Lists.newArrayList(ids)
                .stream()
                .map(id -> {
                    Optional<T> find = findById(id);
                    return find.orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return local.size();
    }

    @Override
    public void deleteById(ID id) {
        local.remove(id);
    }

    @Override
    public void delete(T entity) {
        ID id = metadata.getId(entity);
        if (id != null) {
            local.remove(id);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        local.clear();
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

}
