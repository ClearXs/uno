package cc.allio.uno.data.orm.executor.interceptor;

import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.dsl.Operator;

/**
 * 内置拦截器，在command操作之后进行拦截
 *
 * @author j.x
 * @since 1.1.7
 */
public class BeforeInterceptor extends InternalInterceptor {
    public BeforeInterceptor(Interceptor interceptor) {
        super(interceptor);
    }

    @Override
    protected void onSave(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onSaveBefore(commandExecutor, operator);
    }

    @Override
    protected void onUpdate(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onUpdateBefore(commandExecutor, operator);
    }

    @Override
    protected void onDelete(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onDeleteBefore(commandExecutor, operator);
    }

    @Override
    protected void onQuery(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        interceptor.onQueryBefore(commandExecutor, operator);
    }
}
