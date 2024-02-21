package cc.allio.uno.data.orm.executor.elasticsearch;

import cc.allio.uno.data.orm.executor.ResultGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展{@link ResultGroup}。增加聚合结果集
 *
 * @author jiangwei
 * @date 2023/5/28 21:41
 * @since 1.1.4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EsResultGroup extends ResultGroup {

    /**
     * es索引
     */
    private String index;

    /**
     * es类型
     */
    private String type;

    /**
     * id
     */
    private String id;

    /**
     * score
     */
    private double score;

    /**
     * 当前数据所属于节点id
     */
    private String nodeId;

    /**
     * 当前节点所属于分片数据
     */
    private String shardId;

}
