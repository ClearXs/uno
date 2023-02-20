package cc.allio.uno.data.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

/**
 * 水位站-水势
 *
 * @author jiangwei
 * @date 2022/1/19 15:55
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum WaterPotentialEnum {

	/**
	 * 水势-涨
	 */
	UP("UP", "涨"),

	/**
	 * 水势-落
	 */
	DOWN("DOWN", "落"),

	/**
	 * 水势-平
	 */
	KEEP("KEEP", "平"),

	/**
	 * 水势-未知
	 */
	UNKNOWN("UNKNOWN", "未知");

	@EnumValue
	@JsonValue
	private String value;

	private String label;

	public static String getLabel(String value) {
		for (WaterPotentialEnum sourceEnum : values()) {
			if (sourceEnum.getValue().equals(value)) {
				return sourceEnum.getLabel();
			}
		}
		return null;
	}

	public static String getValue(String code) {
		for (WaterPotentialEnum sourceEnum : values()) {
			if (sourceEnum.getLabel().equals(code)) {
				return sourceEnum.getValue();
			}
		}
		return null;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static WaterPotentialEnum jsonCreator(@JsonProperty("value") String value) {
		if (!ObjectUtils.isEmpty(value)) {
			for (WaterPotentialEnum item : values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return null;
	}
}
