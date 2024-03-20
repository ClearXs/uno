package cc.allio.uno.data.orm.executor.elasticsearch.internal;

import cc.allio.uno.data.orm.executor.ResultSet;
import lombok.Getter;
import lombok.Setter;

/**
 * es结果集
 *
 * @author j.x
 * @date 2023/5/28 22:46
 * @since 1.1.4
 */
@Getter
public class EsResultSet extends ResultSet {

    /**
     * 聚合结果集
     */
    @Setter
    private AggrResultSet aggrResultSet;

    private final double maxScore;
    private final long total;
    private final String relation;

    public EsResultSet(double maxScore, long total, String relation) {
        this.maxScore = maxScore;
        this.total = total;
        this.relation = relation;
    }
}
