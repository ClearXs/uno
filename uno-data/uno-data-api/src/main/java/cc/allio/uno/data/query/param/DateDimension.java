package cc.allio.uno.data.query.param;

import lombok.Data;

/**
 * 数据抽稀维度
 *
 * @author j.x
 * @since 1.1.0
 */
@Data
public class DateDimension {

    /**
     * 时间数据
     */
    private String date;

    /**
     * 维度数据
     */
    private TimeDimension dimension;
}
