package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import cc.allio.uno.data.query.QueryFilter;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 基于{@link CommandExecutor}的流
 *
 * @author jiangwei
 * @date 2023/4/21 13:21
 * @since 1.1.4
 */
public class SQLCommandExecutorStream implements CollectionTimeStream<Map<String, Object>> {

    private final CommandExecutor commandExecutor;

    public SQLCommandExecutorStream(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Flux<Map<String, Object>> read(QueryFilter queryFilter) throws Throwable {
        if (queryFilter instanceof QueryOperator) {
            return new CollectionTimeStreamImpl<>(commandExecutor.queryListMap((QueryOperator) queryFilter)).read(queryFilter);
        }
        return Flux.empty();
    }
}
