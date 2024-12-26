package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;

/**
 * IO({@link InsertOperator}) inner command executor
 *
 * @author j.x
 * @since 1.1.7
 */
public interface IOInnerCommandExecutor<O extends InsertOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, IOInnerCommandExecutor.class);
    }
}
