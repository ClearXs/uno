package cc.allio.uno.data.query;

import cc.allio.uno.data.orm.executor.AggregateCommandExecutor;
import cc.allio.uno.data.orm.executor.CommandExecutor;
import cc.allio.uno.data.query.stream.*;

import java.util.Collection;
import java.util.Map;

/**
 * 高阶查询Impl
 *
 * @author j.x
 * @since 1.1.4
 */
public class BaseHigherQuery implements HigherQuery {

    private final AggregateCommandExecutor sqlExecutor;

    public BaseHigherQuery(AggregateCommandExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public <T> Collection<T> queryList(QueryFilter queryFilter) throws Throwable {
        AsyncStream<Map<String, Object>> dataStream =
                new StreamBuilder<>(new SQLCommandExecutorStream(sqlExecutor))
                        .sort()
                        .supplement()
                        .outliersIgnore()
                        .diluent()
                        .buildAsync();
        return (Collection<T>) dataStream.read(queryFilter);
    }

    @Override
    public Map<String, Collection<?>> queryContemporaneous(QueryFilter queryFilter) throws Throwable {
        CollectionTimeStream<Map<String, Object>> sourceStream =
                new StreamBuilder<>(new SQLCommandExecutorStream(sqlExecutor))
                        .sort()
                        .supplement()
                        .outliersIgnore()
                        .diluent()
                        .build();
        return new ContemporaneousStream(sourceStream).read(queryFilter);
    }

    @Override
    public Map<String, Collection<ValueTime>> queryListForValueTime(QueryFilter queryFilter) throws Throwable {
        CollectionTimeStream<Map<String, Object>> sourceStream =
                new StreamBuilder<>(new SQLCommandExecutorStream(sqlExecutor))
                        .sort()
                        .supplement()
                        .outliersIgnore()
                        .diluent()
                        .build();
        return new ValueTimeStream(sourceStream).read(queryFilter);

    }

    @Override
    public Map<String, Map<String, Collection<ValueTime>>> queryContemporaneousForValueTime(QueryFilter queryFilter) throws Throwable {
        CollectionTimeStream<Map<String, Object>> sourceStream =
                new StreamBuilder<>(new SQLCommandExecutorStream(sqlExecutor))
                        .sort()
                        .supplement()
                        .outliersIgnore()
                        .diluent()
                        .build();
        return new ContemporaneousValueTimeStream(new ContemporaneousStream(sourceStream)).read(queryFilter);
    }
}
