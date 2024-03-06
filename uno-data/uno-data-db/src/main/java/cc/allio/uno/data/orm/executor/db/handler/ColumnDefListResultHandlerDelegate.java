package cc.allio.uno.data.orm.executor.db.handler;

import cc.allio.uno.auto.service.AutoService;
import cc.allio.uno.data.orm.dsl.ColumnDef;
import cc.allio.uno.data.orm.dsl.type.DBType;
import cc.allio.uno.data.orm.executor.ResultSet;
import cc.allio.uno.data.orm.executor.db.handler.dialect.H2ColumnDefListResultSetHandler;
import cc.allio.uno.data.orm.executor.db.handler.dialect.MySQLColumnDefListResultHandler;
import cc.allio.uno.data.orm.executor.db.handler.dialect.PostgreSQLColumnDefListResultSetHandler;
import cc.allio.uno.data.orm.executor.handler.ColumnDefListResultSetHandler;
import cc.allio.uno.data.orm.executor.options.ExecutorOptions;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 基于db的多类型，构建一个代理类，按照不同的{@link DBType}构建对应的{@link ColumnDefListResultSetHandler}
 *
 * @author jiangwei
 * @date 2024/2/14 22:58
 * @since 1.1.7
 */
@AutoService(ColumnDefListResultSetHandler.class)
public class ColumnDefListResultHandlerDelegate extends ColumnDefListResultSetHandler {

    private final Map<DBType, ColumnDefListResultSetHandler> dialectRegistry = Maps.newHashMap();

    public ColumnDefListResultHandlerDelegate() {
        dialectRegistry.put(DBType.H2, new H2ColumnDefListResultSetHandler());
        dialectRegistry.put(DBType.MYSQL, new MySQLColumnDefListResultHandler());
        dialectRegistry.put(DBType.POSTGRESQL, new PostgreSQLColumnDefListResultSetHandler());
    }

    @Override
    public List<ColumnDef> apply(ResultSet resultSet) {
        ExecutorOptions executorOptions = obtainExecutorOptions();
        DBType dbType = executorOptions.getDbType();
        ColumnDefListResultSetHandler actual = dialectRegistry.get(dbType);
        if (actual != null) {
            actual.setExecutorOptions(executorOptions);
            return actual.apply(resultSet);
        }
        return super.apply(resultSet);
    }
}
