package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.Operator;
import org.junit.jupiter.api.Order;

@Order(Integer.MAX_VALUE)
public class PrintInterceptor implements Interceptor {

    @Override
    public void onSaveBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        System.out.println(String.format("on save before commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onSaveAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
        System.out.println(String.format("on save after commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onUpdateBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        System.out.println(String.format("on update before commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onUpdateAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
        System.out.println(String.format("on update after commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onDeleteBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        System.out.println(String.format("on delete before commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onDeleteAfter(CommandExecutor commandExecutor, Operator<?> operator, boolean result) {
        System.out.println(String.format("on delete after commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onQueryBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        System.out.println(String.format("on query before commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onQueryAfter(CommandExecutor commandExecutor, Operator<?> operator, Object result) {
        System.out.println(String.format("on query after commandExecutor \n %s", operator.getDSL()));
    }

    @Override
    public void onUnknownCommand(CommandExecutor commandExecutor, CommandType commandType, Operator<?> operator) {
        System.out.println(String.format("on unknown command command type [%s] \n %s ", operator.getDSL(), commandType));
    }
}
