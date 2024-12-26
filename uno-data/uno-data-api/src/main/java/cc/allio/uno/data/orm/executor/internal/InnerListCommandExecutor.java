package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;

import java.util.List;

/**
 * base on {@link ListResultSetHandler} build to command executor
 *
 * @author j.x
 * @since 1.1.7
 */
public interface InnerListCommandExecutor<R, O extends Operator> extends InnerCommandExecutor<List<R>, O, ListResultSetHandler<R>> {
}
