package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultSet;

import java.util.List;
import java.util.function.Function;

/**
 * List 结果集处理器
 *
 * @author j.x
 * @date 2023/4/18 13:29
 * @since 1.1.4
 */
public interface ListResultSetHandler<R> extends Function<ResultSet, List<R>>, ResultHandler {

}
