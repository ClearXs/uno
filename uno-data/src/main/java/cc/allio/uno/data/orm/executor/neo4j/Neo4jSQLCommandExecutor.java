package cc.allio.uno.data.orm.executor.neo4j;

import cc.allio.uno.data.orm.sql.OperatorMetadata;
import cc.allio.uno.data.orm.sql.SQLOperator;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.orm.executor.ListResultSetHandler;
import cc.allio.uno.data.orm.executor.ResultSetHandler;
import cc.allio.uno.data.orm.executor.SQLCommandType;
import cc.allio.uno.data.orm.executor.SQLCommandExecutor;

import java.util.List;

/**
 * neo4j sql执行器
 *
 * @author jiangwei
 * @date 2023/4/19 12:10
 * @since 1.1.4
 */
public class Neo4jSQLCommandExecutor implements SQLCommandExecutor {
    @Override
    public boolean bool(SQLOperator<?> operator, SQLCommandType sqlCommand, ResultSetHandler<Boolean> resultSetHandler) {
        return false;
    }

    @Override
    public <R> List<R> queryList(SQLQueryOperator queryOperator, ListResultSetHandler<R> resultSetHandler) {
        return null;
    }

    @Override
    public ExecutorKey getKey() {
        return SQLCommandExecutor.NEO4J_SQL_COMMAND_KEY;
    }

    @Override
    public OperatorMetadata getOperatorMetadata() {
        return null;
    }
}
