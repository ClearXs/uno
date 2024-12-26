package cc.allio.uno.data.orm.dsl;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.ClassUtils;

/**
 * definition 'Meta data'
 *
 * @author j.x
 * @since 1.1.8
 */
public interface Meta<T extends Meta<T>> {

    /**
     * meta accept {@link MetaAcceptor}
     *
     * @param acceptor the acceptor instance
     */
    default void accept(MetaAcceptor<T> acceptor) {
        if (acceptor != null) {
            acceptor.onAccept((T) this);
        }
    }

    /**
     * dynamic create meta instance by {@link Class} metaclass and args
     *
     * @param metaClass the metaclass
     * @param args      the args
     * @param <P>       the meta subtype
     * @return {@link Meta} instance
     */
    static <P extends Meta<P>> P create(Class<P> metaClass, Object... args) {
        return ClassUtils.newInstance(metaClass, args);
    }

    /**
     * dynamic create meta instance by {@link Class} {@link Meta} and args.
     * <b>this method will be through {@link MetaAcceptor} enhance meta data</b>
     *
     * @param metaClass       the metaclass
     * @param metaAcceptorSet the {@link MetaAcceptorSet} instance
     * @param args            the args
     * @param <P>             the meta subtype
     * @return {@link Meta} instance
     * @throws NullPointerException if create metaclass is empty
     */
    static <P extends Meta<P>> P create(Class<P> metaClass, MetaAcceptorSet metaAcceptorSet, Object... args) {
        P meta = ClassUtils.newInstance(metaClass, args);
        if (meta == null) {
            throw Exceptions.unNull(String.format("Failed to create %s meta instance", metaClass.getName()));
        }
        if (metaAcceptorSet != null) {
            metaAcceptorSet.adapt(meta);
        }
        return meta;
    }
}
