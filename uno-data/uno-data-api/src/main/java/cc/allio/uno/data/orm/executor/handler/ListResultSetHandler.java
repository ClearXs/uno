package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.result.ResultSet;

import java.util.List;
import java.util.function.Function;

/**
 * List 结果集处理器
 *
 * @author j.x
 * @since 1.1.4
 * @see ResultSetHandler
 */
public interface ListResultSetHandler<R> extends Function<ResultSet, List<R>>, ResultHandler {

    /**
     * get result type
     *
     * @since 1.1.9
     * @return the result type
     */
    Class<R> getResultType();
}
