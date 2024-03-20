package cc.allio.uno.core.type;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 基于内存的类型管理器
 *
 * @author j.x
 * @date 2023/4/11 19:57
 * @since 1.1.4
 */
public class MemoryTypeManager implements TypeManager {

    private final Set<Type> caches = Sets.newCopyOnWriteArraySet();

    @Override
    public void add(Type type) {
        caches.add(type);
    }

    @Override
    public void addAll(Type... types) {
        addAll(Lists.newArrayList(types));
    }

    @Override
    public void addAll(List<Type> types) {
        caches.addAll(types);
    }

    @Override
    public boolean contains(Type type) {
        return caches.contains(type);
    }

    @Override
    public boolean contains(String code) {
        return caches.contains(DefaultType.of(code));
    }

    @Override
    public Collection<Type> findAll() {
        return caches;
    }

    @Override
    public Optional<Type> findOne(String code) {
        if (contains(code)) {
            return caches.stream().filter(type -> type.equals(DefaultType.of(code))).findFirst();
        }
        return Optional.empty();
    }
}
