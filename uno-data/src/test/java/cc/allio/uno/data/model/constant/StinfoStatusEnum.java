package cc.allio.uno.data.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author ljl
 * @since 2021-08-13
 */
@Getter
@AllArgsConstructor
public enum StinfoStatusEnum {

	ONLINE("ONLINE", "在线"),
	OFFLINE("OFFLINE", "离线");

	//离线:0,在线:1
	@EnumValue
	@JsonValue
	private String value;

	private String label;

	/**
	 * 解决前端传入为空 导致程序异常的问题
	 *
	 * @param value
	 */
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static StinfoStatusEnum jsonCreator(@JsonProperty("value") String value) {
		if (null != value) {
			for (StinfoStatusEnum item : values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return null;
	}

	public static StinfoStatusEnum getEnumByCode(String label) {
		for (StinfoStatusEnum item : values()) {
			if (item.getLabel().equals(label)) {
				return item;
			}
		}
		return null;
	}

	public static String getLabel(String value)
	{
		for (StinfoStatusEnum stinfoStatus : values())
		{
			if (stinfoStatus.getValue().equals(value))
				return stinfoStatus.getLabel();
		}
		return null;
	}
}
