package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.core.util.Values;
import cc.allio.uno.data.orm.dsl.ddl.AlterTableOperator;
import org.slf4j.Logger;

/**
 * ATO({@link AlterTableOperator}) inner command type
 *
 * @author j.x
 * @since 1.1.7
 */
public interface ATOInnerCommandExecutor<O extends AlterTableOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, ATOInnerCommandExecutor.class);
    }
}
