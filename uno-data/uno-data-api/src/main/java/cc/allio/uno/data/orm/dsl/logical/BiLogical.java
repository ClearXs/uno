package cc.allio.uno.data.orm.dsl.logical;

/**
 * the binary of logical
 *
 * @author j.x
 * @since 1.1.7
 */
public interface BiLogical<R, T> {

    /**
     * doAccept method
     *
     * @param p argument
     * @return the R result
     */
    R doAccept(T p);

    /**
     * get logical predicate
     *
     * @return logical
     */
    Logical getLogical();
}
