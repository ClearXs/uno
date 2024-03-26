package cc.allio.uno.data.orm.executor.internal;

import cc.allio.uno.core.reflect.ReflectTools;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.ddl.ShowColumnsOperator;

/**
 * SCO({@link ShowColumnsOperator}) inner command type
 *
 * @author j.x
 * @date 2024/3/15 04:36
 * @since 1.1.7
 */
public interface SCOInnerCommandExecutor<R, O extends ShowColumnsOperator> extends InnerListCommandExecutor<R, O> {

    @Override
    default Class<O> getRealityOperatorType() {
        return (Class<O>) ReflectTools.getGenericType(this, SCOInnerCommandExecutor.class, 1);
    }
}
