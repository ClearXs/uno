package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.ResultSet;
import lombok.Getter;
import lombok.Setter;

/**
 * es结果集
 *
 * @author jiangwei
 * @date 2023/5/28 22:46
 * @since 1.1.4
 */
public class EsResultSet extends ResultSet {

    /**
     * 聚合结果集
     */
    @Setter
    @Getter
    private AggrResultSet aggrResultSet;

    @Getter
    private final double maxScore;
    @Getter
    private final long total;
    @Getter
    private final String relation;

    public EsResultSet(double maxScore, long total, String relation) {
        this.maxScore = maxScore;
        this.total = total;
        this.relation = relation;
    }

}
