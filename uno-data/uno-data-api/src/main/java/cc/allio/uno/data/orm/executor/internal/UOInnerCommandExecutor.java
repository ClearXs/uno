package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;

/**
 * UO({@link UpdateOperator}) inner command executor
 *
 * @author j.x
 * @date 2024/3/15 04:40
 * @since 1.1.7
 */
public interface UOInnerCommandExecutor<O extends UpdateOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, UOInnerCommandExecutor.class);
    }
}
