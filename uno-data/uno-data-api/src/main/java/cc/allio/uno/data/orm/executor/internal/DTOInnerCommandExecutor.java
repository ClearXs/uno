package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.ddl.DropTableOperator;

/**
 * DTO({@link DropTableOperator}) inner command executor
 *
 * @author j.x
 * @date 2024/3/15 04:34
 * @since 1.1.7
 */
public interface DTOInnerCommandExecutor<O extends DropTableOperator> extends InnerDefaultCommandExecutor<Boolean, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, DTOInnerCommandExecutor.class);
    }
}
