package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import lombok.Getter;

import java.util.Map;

/**
 * es聚合结果集
 *
 * @author j.x
 * @since 1.1.4
 */
public class AggrResultSet {

    @Getter
    private final Map<String, Aggregate> origin;

    public AggrResultSet(Map<String, Aggregate> origin) {
        this.origin = origin;
    }
}
