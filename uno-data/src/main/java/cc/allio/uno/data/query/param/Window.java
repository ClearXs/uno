package cc.allio.uno.data.query.param;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 数据抽稀相关常量
 *
 * @author jiangwei
 * @date 2022/10/9 11:25
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum Window {


    /**
     * 30分钟抽稀维度
     */
    THIRTY_MINUTES("30M", "30分钟", () -> Duration.ofMinutes(30)),

    /**
     * 1小时抽稀维度
     */
    ONE_HOUR("1H", "1小时", () -> Duration.ofHours(1)),

    /**
     * 3小时抽稀维度
     */
    THREE_HOUR("3H", "3小时", () -> Duration.ofHours(3)),

    /**
     * 6小时抽稀维度
     */
    SIX_HOUR("6H", "6小时", () -> Duration.ofHours(6)),

    /**
     * 12小时抽稀维度
     */
    TWELVE_HOUR("12H", "12小时", () -> Duration.ofHours(12)),

    /**
     * 24小时抽稀维度
     */
    TWENTY_FOUR_HOUR("24H", "24小时", () -> Duration.ofHours(24)),

    /**
     * 3天抽稀维度
     */
    THREE_DAY("3D", "3天", () -> Duration.ofDays(1L)),

    /**
     * 一周
     */
    WEEK("WEEK", "一周", () -> Duration.ofDays(7L));

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
     * 时间间隔
     */
    private final Supplier<Duration> duration;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Window jsonCreator(@JsonProperty("value") String value) {
        if (null != value) {
            for (Window item : values()) {
                if (item.value.equals(value)) {
                    return item;
                }
            }
        }
        return null;
    }
}
