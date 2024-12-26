package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.Table;
import cc.allio.uno.data.orm.dsl.ddl.ShowTablesOperator;

/**
 * ST({@link ShowTablesOperator}) command executor
 *
 * @author j.x
 * @since 1.1.7
 */
public interface STInnerCommandExecutor<R, O extends ShowTablesOperator> extends InnerListCommandExecutor<R, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, STInnerCommandExecutor.class, 1);
    }
}
