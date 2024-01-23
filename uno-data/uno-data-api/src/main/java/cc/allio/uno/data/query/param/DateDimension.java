package cc.allio.uno.data.query.param;

import lombok.Data;

/**
 * 数据抽稀维度
 *
 * @author jiangwei
 * @date 2022/11/16 11:40
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
