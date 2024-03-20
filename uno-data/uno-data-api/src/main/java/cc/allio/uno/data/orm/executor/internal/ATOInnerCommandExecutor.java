package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;

/**
 * ATO({@link AlterTableOperator}) inner command type
 *
 * @author j.x
 * @date 2024/3/15 10:49
 * @since 1.1.7
 */
public interface ATOInnerCommandExecutor<O extends AlterTableOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, ATOInnerCommandExecutor.class);
    }
}
