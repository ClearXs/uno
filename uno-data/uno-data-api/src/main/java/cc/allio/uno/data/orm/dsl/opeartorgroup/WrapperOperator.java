package cc.allio.uno.data.orm.dsl.opeartorgroup;

import cc.allio.uno.data.orm.dsl.Operator;

/**
 * describe how to wrapper {@link Operator}
 *
 * @author j.x
 * @since 1.1.8
 */
public interface WrapperOperator {

    /**
     * get actual {@link Operator}
     *
     * @param <T> the {@link Operator} type
     * @return actual operator
     */
    <T extends Operator<T>> T getActual();
}
