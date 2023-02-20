package cc.allio.uno.data.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


/**
 * 报警状态
 */
@Getter
@AllArgsConstructor
public enum AlarmStatusEnum {
	/**
	 * 正常:0,报警:1
	 */
	NORMAL("NORMAL", "正常"),
	/**
	 * 1 报警
	 */
	ALARM("ALARM", "报警");

	@JsonValue
	@EnumValue
	private String value;

	private String label;

	public static String getLabel(Integer value) {
		for (AlarmStatusEnum workStatus : values()) {
			if (workStatus.value.equals(value)) {
				return workStatus.label;
			}
		}
		return null;
	}

	public static String getValue(String code) {
		for (AlarmStatusEnum workStatus : values()) {
			if (Objects.equals(workStatus.value, code)) {
				return workStatus.value;
			}
		}
		return null;
	}

	public static AlarmStatusEnum getEnumByCode(String code) {
		for (AlarmStatusEnum workStatus : values()) {
			if (workStatus.label.equals(code))
				return workStatus;
		}
		return null;
	}

	/**
	 * 解决前端传入为空 导致程序异常的问题
	 *
	 * @param value
	 */
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static AlarmStatusEnum jsonCreator(String value) {
		if (null != value) {
			for (AlarmStatusEnum item : values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return null;
	}
}
