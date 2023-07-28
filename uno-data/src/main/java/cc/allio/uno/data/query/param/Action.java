package cc.allio.uno.data.query.param;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 抽稀动作
 *
 * @author jiangwei
 * @date 2022/10/11 10:59
 * @since 1.1.0
 */
@Getter
@AllArgsConstructor
public enum Action {

    /**
     * 默认
     */
    DEFAULT("DEFAULT", "默认", new DefaultDiluteAction()),

    /**
     * 数据相加
     */
    ADD("ADD", "相加", new AddDiluteAction());

    /**
     * 动作标识
     */
    @JsonValue
    @EnumValue
    private final String value;

    /**
     * 动作名称
     */
    private final String label;

    /**
     * 抽稀动作
     */
    private final DiluteAction action;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Action jsonCreator(@JsonProperty("value") String value) {
        if (null != value) {
            for (Action item : values()) {
                if (item.value.equals(value)) {
                    return item;
                }
            }
        }
        return null;
    }
}
