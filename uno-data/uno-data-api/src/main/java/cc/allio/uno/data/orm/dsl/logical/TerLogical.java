package cc.allio.uno.data.orm.dsl.logical;

/**
 * the ternary logical
 *
 * @author j.x
 * @since 1.1.7
 */
public interface TerLogical<R, T1, T2> {

    /**
     * doAccept method
     *
     * @param p1 the first argument
     * @param p2 the second argument
     * @return the R result
     */
    R doAccept(T1 p1, T2 p2);

    /**
     * get logical predicate
     *
     * @return logical
     */
    Logical getLogical();
}
