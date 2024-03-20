package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator;

/**
 * CTO({@link cc.allio.uno.data.orm.dsl.ddl.CreateTableOperator}) inner command executor
 *
 * @author j.x
 * @date 2024/3/15 04:26
 * @since 1.1.7
 */
public interface CTOInnerCommandExecutor<O extends CreateTableOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, CTOInnerCommandExecutor.class);
    }
}
