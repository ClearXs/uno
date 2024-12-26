package cc.allio.uno.data.orm.dsl.logical;

/**
 * the quaternary logical
 *
 * @author j.x
 * @since 1.1.7
 */
public interface QueLogical<R, T1, T2, T3> {

    /**
     * doAccept method
     *
     * @param p1 the first argument
     * @param p2 the second argument
     * @param p3 the third argument
     * @return the R result
     */
    R doAccept(T1 p1, T2 p2, T3 p3);

    /**
     * get logical predicate
     *
     * @return logical
     */
    Logical getLogical();
}
