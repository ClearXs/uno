package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;

/**
 * QO({@link QueryOperator}) inner command executor
 *
 * @author j.x
 * @since 1.1.7
 */
public interface QOInnerCommandExecutor<R, O extends QueryOperator> extends InnerListCommandExecutor<R, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, QOInnerCommandExecutor.class);
    }
}
