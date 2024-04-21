package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;

/**
 * base on {@link ResultSetHandler} build to command executor
 *
 * @author j.x
 * @date 2024/3/15 06:42
 * @since 1.1.7
 */
public interface InnerDefaultCommandExecutor<R, O extends Operator> extends InnerCommandExecutor<R, O, ResultSetHandler<R>> {
}
