package cc.allio.uno.data.orm.dsl;

import com.google.common.collect.Maps;

import java.util.Map;

public class MetaAcceptorSetImpl implements MetaAcceptorSet {

    private final Map<Class<? extends Meta<?>>, MetaAcceptor<? extends Meta<?>>> cache;

    public MetaAcceptorSetImpl() {
        this.cache = Maps.newConcurrentMap();
    }


    @Override
    public <T extends Meta<T>> void customizeMetaAcceptorSetter(Class<T> metaClass, MetaAcceptor<T> metaAcceptor) {
        cache.put(metaClass, metaAcceptor);
    }

    @Override
    public <T extends Meta<T>> MetaAcceptor<T> customizeMetaAcceptorGetter(Class<T> metaClass) {
        return (MetaAcceptor<T>) cache.get(metaClass);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
