package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.orm.executor.SQLCommandExecutor;
import cc.allio.uno.data.orm.sql.dml.SQLQueryOperator;
import cc.allio.uno.data.query.mybatis.QueryFilter;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 基于{@link SQLCommandExecutor}的流
 *
 * @author jiangwei
 * @date 2023/4/21 13:21
 * @since 1.1.4
 */
public class SQLCommandExecutorStream implements CollectionTimeStream<Map<String, Object>> {

    private final SQLCommandExecutor sqlCommandExecutor;

    public SQLCommandExecutorStream(SQLCommandExecutor sqlCommandExecutor) {
        this.sqlCommandExecutor = sqlCommandExecutor;
    }

    @Override
    public Flux<Map<String, Object>> read(QueryFilter queryFilter) throws Throwable {
        if (queryFilter instanceof SQLQueryOperator) {
            return new CollectionTimeStreamImpl<>(sqlCommandExecutor.queryListMap((SQLQueryOperator) queryFilter)).read(queryFilter);
        }
        return Flux.empty();
    }
}
