package cc.allio.uno.data.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * 物联网推送数据操作
 *
 * @author heitianzhen
 * @date 2022/3/25 9:41
 */
@Getter
@AllArgsConstructor
public enum IoTOperate {
	/**
	 * 雨量水位一体化站
	 */
	ONLINE("ONLINE", "设备上线"),

	/**
	 * 离线
	 */
	OFFLINE("OFFLINE", "设备下线"),

	/**
	 * 上报
	 */
	REPORT_PROPERTY("REPORT_PROPERTY", "属性上报");


	@EnumValue
	@JsonValue
	private String value;

	private String label;

	public String getValue() {
		return value;
	}

	public static String getLabel(String value) {
		for (IoTOperate operationStatus : values()) {
			if (operationStatus.getValue().equals(value)) {
				return operationStatus.getLabel();
			}
		}
		return null;
	}

	/**
	 * 解决前端传入为空 导致程序异常的问题
	 * 反序列化时，调用 @JsonCreator 标注的构造器或者工厂方法来创建对象
	 */
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static IoTOperate jsonCreator(@JsonProperty("value") String value) {
		if (!ObjectUtils.isEmpty(value)) {
			for (IoTOperate item : values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return null;
	}

	public static IoTOperate getIoTOperate(String operate) {
		return Arrays.stream(values())
			.filter(ioTOperate -> ioTOperate.getValue().equals(operate))
			.findFirst()
			.orElse(null);
	}

	@Override
	public String toString() {
		return "OperationStatusEnum{" +
			"value='" + value + '\'' +
			", label='" + label + '\'' +
			'}';
	}
}
