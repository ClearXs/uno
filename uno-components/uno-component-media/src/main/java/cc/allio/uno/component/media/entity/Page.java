package cc.allio.uno.component.media.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 数据分页参数
 *
 * @author jiangwei
 * @date 2022/11/25 14:45
 * @since 1.1.2
 */
@Data
public class Page implements Serializable {

    /**
     * 查询数据列表
     */
    protected List<Media> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    private long size = 10;

    /**
     * 当前页
     */
    private long current = 1;
}
