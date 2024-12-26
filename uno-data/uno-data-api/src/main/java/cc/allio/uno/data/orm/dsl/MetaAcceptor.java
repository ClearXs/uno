package cc.allio.uno.data.orm.dsl;

/**
 * definition about dsl meta data acceptor
 * <p>the meta data contains</p>
 * <ul>
 *     <li>{@link Table}</li>
 *     <li>{@link Database}</li>
 *     <li>{@link ColumnDef}</li>
 *     <li>{@link DSLName}</li>
 * </ul>
 *
 * @author j.x
 * @since 1.1.8
 */
public interface MetaAcceptor<T extends Meta<T>> {

    /**
     * trigger accept event
     *
     * @param meta the meta
     */
    void onAccept(T meta);
}
