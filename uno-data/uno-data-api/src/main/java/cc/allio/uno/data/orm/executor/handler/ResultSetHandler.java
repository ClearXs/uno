package cc.allio.uno.data.orm.executor.handler;

import cc.allio.uno.data.orm.executor.ResultGroup;

import java.util.function.Function;

/**
 * DSL执行结果集处理器
 *
 * @author j.x
 * @date 2023/4/18 13:16
 * @since 1.1.4
 */
public interface ResultSetHandler<R> extends Function<ResultGroup, R>, ResultHandler {

}
