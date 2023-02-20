package cc.allio.uno.data.query.param;

import cc.allio.uno.core.util.CalendarUtil;
import cc.allio.uno.core.util.DateUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.function.Function;

/**
 * 维度标识
 *
 * @author jiangwei
 * @date 2022/11/16 11:40
 * @since 1.1.0
 */
@Getter
@AllArgsConstructor
public enum TimeDimension {

    YEAR("YEAR", "年", time -> DateUtil.parse(time, DateUtil.PATTERN_YEAR), CalendarUtil::getFirstDayOfYear, CalendarUtil::getLastDayOfYear),
    MONTH("MONTH", "月", time -> DateUtil.parse(time, DateUtil.PATTERN_MONTH), CalendarUtil::getFirstDayOfMonth, CalendarUtil::getLastDayOfMonth),
    DAY("DAY", "日", time -> DateUtil.parse(time, DateUtil.PATTERN_DATE), CalendarUtil::getFirstDay, CalendarUtil::getLastSecondOfDay);

    /**
     * 时间维度标识
     */
    @JsonValue
    @EnumValue
    private final String value;

    /**
     * 维度名称
     */
    private final String label;

    /**
     * 转换返回为时间
     */
    private final Function<String, Date> toDate;

    /**
     * 当前维度下的起始时间
     */
    private final Function<Date, Date> timeStart;

    /**
     * 当前维度下结束时间
     */
    private final Function<Date, Date> timeEnd;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TimeDimension jsonCreator(@JsonProperty("value") String value) {
        if (null != value) {
            for (TimeDimension item : values()) {
                if (item.value.equals(value)) {
                    return item;
                }
            }
        }
        return null;
    }
}
