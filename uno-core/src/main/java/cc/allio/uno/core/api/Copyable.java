package cc.allio.uno.core.api;

/**
 * define a copy method for object.
 *
 * @param <Self> the self generic type
 * @author j.x
 * @since 1.2.2
 */
public interface Copyable<Self> {

    /**
     * copy method. the implementation must be deep copy current.
     *
     * @return the new instance
     */
    Self copy();
}
