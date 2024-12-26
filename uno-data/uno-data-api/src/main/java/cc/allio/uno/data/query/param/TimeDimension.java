package cc.allio.uno.data.query.param;

import cc.allio.uno.core.util.CalendarUtils;
import cc.allio.uno.core.util.DateUtil;
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
 * @author j.x
 * @since 1.1.0
 */
@Getter
@AllArgsConstructor
public enum TimeDimension {

    YEAR("YEAR", "年", time -> DateUtil.parse(time, DateUtil.PATTERN_YEAR), CalendarUtils::getFirstDayOfYear, CalendarUtils::getLastDayOfYear),
    MONTH("MONTH", "月", time -> DateUtil.parse(time, DateUtil.PATTERN_MONTH), CalendarUtils::getFirstDayOfMonth, CalendarUtils::getLastDayOfMonth),
    DAY("DAY", "日", time -> DateUtil.parse(time, DateUtil.PATTERN_DATE), CalendarUtils::getFirstDay, CalendarUtils::getLastSecondOfDay);

    /**
     * 时间维度标识
     */
    @JsonValue
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
