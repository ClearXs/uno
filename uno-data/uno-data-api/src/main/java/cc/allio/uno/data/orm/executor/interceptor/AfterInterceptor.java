package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.core.type.Types;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.dsl.Operator;

/**
 * 内置拦截器，在Command操作执行之前进行拦截
 *
 * @author jiangwei
 * @date 2024/1/8 12:11
 * @since 1.1.6
 */
public class AfterInterceptor extends InternalInterceptor {

    public AfterInterceptor(Interceptor interceptor) {
        super(interceptor);
    }

    @Override
    protected void onSave(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onSaveAfter(commandExecutor, operator, Types.getBoolean(result));
    }

    @Override
    protected void onUpdate(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onSaveAfter(commandExecutor, operator, Types.getBoolean(result));
    }

    @Override
    protected void onDelete(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onDeleteAfter(commandExecutor, operator, Types.getBoolean(result));
    }

    @Override
    protected void onQuery(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onQueryAfter(commandExecutor, operator, result);
    }
}
