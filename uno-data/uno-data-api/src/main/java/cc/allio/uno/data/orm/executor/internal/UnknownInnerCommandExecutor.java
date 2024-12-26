package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.UnrecognizedOperator;

/**
 * {@link UnrecognizedOperator} inner command executor
 *
 * @author j.x
 * @since 1.1.9
 */
public interface UnknownInnerCommandExecutor<R, O extends UnrecognizedOperator<?>> extends InnerDefaultCommandExecutor<R, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, UnrecognizedOperator.class);
    }
}
