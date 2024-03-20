package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator;

/**
 * ETO({@link cc.allio.uno.data.orm.dsl.ddl.ExistTableOperator}) inner command executor
 *
 * @author j.x
 * @date 2024/3/15 04:35
 * @since 1.1.7
 */
public interface ETOInnerCommandExecutor<O extends ExistTableOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, ETOInnerCommandExecutor.class);
    }
}
