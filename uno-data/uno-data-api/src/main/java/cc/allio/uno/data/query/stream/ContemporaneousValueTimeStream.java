package cc.allio.uno.data.query.stream;

import cc.allio.uno.data.query.QueryFilter;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 以{@link ContemporaneousStream}数据数据流，把其数据转换为ValueTime的数据流
 *
 * @author j.x
 * @since 1.1.0
 */
public class ContemporaneousValueTimeStream implements TimeStream<Map<String, Map<String, Collection<ValueTime>>>> {

    private final ContemporaneousStream stream;

    public ContemporaneousValueTimeStream(ContemporaneousStream stream) {
        this.stream = stream;
    }

    /**
     * Map类型的实体数据转换为时间数据
     *
     * @param queryFilter 原始map数据
     * @see ValueTimeStream
     */
    @Override
    public Map<String, Map<String, Collection<ValueTime>>> read(QueryFilter queryFilter) throws Throwable {
        Map<String, Collection<?>> contemporaneous = stream.read(queryFilter);
        return contemporaneous.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    try {
                        return new ValueTimeStream(new CollectionTimeStreamImpl<>(Flux.fromIterable(e.getValue()))).read(queryFilter);
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                }));
    }
}
