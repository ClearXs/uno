package cc.allio.uno.data.orm.executor.db;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.executor.interceptor.Interceptor;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.dml.InsertOperator;
import cc.allio.uno.data.orm.dsl.dml.UpdateOperator;

import java.util.Date;

public class AuditInterceptor implements Interceptor {

    @Override
    public void onSaveBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof InsertOperator insertOperator) {
            insertOperator.strictFill("id", () -> IdGenerator.defaultGenerator().getNextId());
            insertOperator.strictFill("create_user", 1L);
            insertOperator.strictFill("create_dept", 1L);
            insertOperator.strictFill("create_time", new Date());
            insertOperator.strictFill("update_user", 1L);
            insertOperator.strictFill("update_time", new Date());
            insertOperator.strictFill("is_deleted", 0);
        }
    }

    @Override
    public void onUpdateBefore(CommandExecutor commandExecutor, Operator<?> operator) {
        if (operator instanceof UpdateOperator updateOperator) {
            updateOperator.strictFill("update_user", 1L);
            updateOperator.strictFill("update_time", new Date());
            updateOperator.strictFill("is_deleted", 0);
        }
    }
}
