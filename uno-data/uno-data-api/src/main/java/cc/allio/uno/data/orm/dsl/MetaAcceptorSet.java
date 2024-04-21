package cc.allio.uno.data.orm.dsl;

import java.util.Collection;

/**
 * {@link MetaAcceptor} Set
 *
 * @author j.x
 * @date 2024/4/16 15:36
 * @since 1.1.8
 */
public interface MetaAcceptorSet {

    /**
     * obtain {@link ColumnDef} {@link MetaAcceptor}
     *
     * @return the {@link MetaAcceptor}
     */
    default MetaAcceptor<ColumnDef> obtainColumnDefAcceptor() {
        return customizeMetaAcceptorGetter(ColumnDef.class);
    }

    /**
     * set {@link ColumnDef} {@link MetaAcceptor}
     *
     * @param columnDefAcceptor the column def acceptor
     */
    default void setColumnDefAcceptor(MetaAcceptor<ColumnDef> columnDefAcceptor) {
        customizeMetaAcceptorSetter(ColumnDef.class, columnDefAcceptor);
    }

    /**
     * obtain {@link Database} {@link MetaAcceptor}
     *
     * @return the {@link MetaAcceptor}
     */
    default MetaAcceptor<Database> obtainDatabaseAcceptor() {
        return customizeMetaAcceptorGetter(Database.class);
    }

    /**
     * set {@link Database} {@link MetaAcceptor}
     *
     * @param databaseDefAcceptor the database acceptor
     */
    default void setDatabaseDefAcceptor(MetaAcceptor<Database> databaseDefAcceptor) {
        customizeMetaAcceptorSetter(Database.class, databaseDefAcceptor);
    }

    /**
     * obtain {@link DSLName} {@link MetaAcceptor}
     *
     * @return the {@link MetaAcceptor}
     */
    default MetaAcceptor<DSLName> obtainDSLNameAcceptor() {
        return customizeMetaAcceptorGetter(DSLName.class);
    }

    /**
     * set {@link DSLName} {@link MetaAcceptor}
     *
     * @param dslNameMetaAcceptor the dsl name acceptor
     */
    default void setDSLNameAcceptor(MetaAcceptor<DSLName> dslNameMetaAcceptor) {
        customizeMetaAcceptorSetter(DSLName.class, dslNameMetaAcceptor);
    }

    /**
     * obtain {@link Table} {@link MetaAcceptor}
     *
     * @return the {@link MetaAcceptor}
     */
    default MetaAcceptor<Table> obtainTableAcceptor() {
        return customizeMetaAcceptorGetter(Table.class);
    }

    /**
     * set {@link Table} {@link MetaAcceptor}
     *
     * @param tableAcceptor the table acceptor
     */
    default void setTableAcceptor(MetaAcceptor<Table> tableAcceptor) {
        customizeMetaAcceptorSetter(Table.class, tableAcceptor);
    }

    /**
     * customize meta acceptor setter.
     *
     * @param metaClass    the meta class
     * @param metaAcceptor the meta acceptor
     * @param <T>          the sub {@link Meta}
     */
    <T extends Meta<T>> void customizeMetaAcceptorSetter(Class<T> metaClass, MetaAcceptor<T> metaAcceptor);

    /**
     * customize meta acceptor getter
     *
     * @param metaClass the metaclass
     * @param <T>       the meta subtype
     * @return the {@link MetaAcceptor} instance
     */
    <T extends Meta<T>> MetaAcceptor<T> customizeMetaAcceptorGetter(Class<T> metaClass);

    /**
     * batch handle {@link Meta} {@link MetaAcceptor}
     *
     * @param meta the {@link Meta} instance
     * @param <T>  the {@link Meta} type
     * @see #adapt(Meta)
     */
    default <T extends Meta<T>> void adapt(Collection<T> meta) {
        meta.forEach(this::adapt);
    }

    /**
     * choose adaption {@link Meta} {@link MetaAcceptor}
     *
     * @param meta the {@link Meta} instance
     * @param <T>  the {@link Meta} type
     */
    default <T extends Meta<T>> void adapt(T meta) {
        if (meta != null) {
            MetaAcceptor<T> acceptor = customizeMetaAcceptorGetter(meta.getClass());
            meta.accept(acceptor);
        }
    }

    /**
     * clear all {@link MetaAcceptor}
     */
    void clear();
}
