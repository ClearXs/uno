package cc.allio.uno.data.test.executor;

import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.data.orm.dsl.Operator;
import cc.allio.uno.data.orm.dsl.opeartorgroup.OperatorGroup;
import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandType;
import cc.allio.uno.data.orm.executor.handler.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorKey;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;

import java.net.SocketTimeoutException;
import java.util.List;

public class FakeCommandExecutor implements AggregateCommandExecutor {

    @Override
    public boolean bool(Operator<?> operator, CommandType commandType, ResultSetHandler<Boolean> resultSetHandler) {
        throw Exceptions.unOperate("bool");
    }

    @Override
    public <R> List<R> queryList(Operator<?> operator, CommandType commandType, ListResultSetHandler<R> resultSetHandler) {
        throw Exceptions.unOperate("queryList");
    }

    @Override
    public boolean check() throws SocketTimeoutException {
        return false;
    }

    @Override
    public ExecutorKey getKey() {
        return null;
    }

    @Override
    public OperatorGroup getOperatorGroup() {
        return null;
    }

    @Override
    public ExecutorOptions getOptions() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
