package cc.allio.uno.data.orm.executor.elasticsearch;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import lombok.Getter;

import java.util.Map;

/**
 * es聚合结果集
 *
 * @author jiangwei
 * @date 2023/5/28 21:42
 * @since 1.1.4
 */
public class AggrResultSet {

    @Getter
    private final Map<String, Aggregate> origin;

    public AggrResultSet(Map<String, Aggregate> origin) {
        this.origin = origin;
    }
}
