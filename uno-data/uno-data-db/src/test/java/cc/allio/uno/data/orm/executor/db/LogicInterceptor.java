package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;

public class LogicInterceptor implements Interceptor {

    @Override
    public void onDeleteBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof UpdateOperator) {
            ((UpdateOperator) operator).strictFill("is_deleted", 1);
        }
    }

    @Override
    public void onQueryBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof QueryOperator) {
            ((QueryOperator) operator).eq("is_deleted", 0);
        }
    }
}
