package cc.allio.uno.data.model.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.ObjectUtils;


/**
 * @author ljl
 * @since 2021-08-13
 */
@Getter
@AllArgsConstructor
public enum SttpTypeEnum {
	/**
	 * 雨量水位一体化站
	 */
	PZ("PZ", "雨量水位一体化站"),

	/**
	 * 流量站
	 */
	SG("SG", "流量站"),

	/**
	 * 雨量站
	 */
	PP("PP", "雨量站"),

	/**
	 * 水位站
	 */
	ZZ("ZZ", "水位站"),

	/**
	 * 水质站
	 */
	WQ("WQ", "水质站"),

	/**
	 * 墒情站
	 */
	SS("SS", "墒情站"),

	/**
	 * 地下水位站
	 */
	ZG("ZG", "地下水位站"),

	/**
	 * 村庄远传水表
	 */
	VWM("VWM", "村庄远传水表"),

	/**
	 * 管网流量站
	 */
	PSG("PSG", "管网流量站"),

	/**
	 * 流量计
	 */
	ESG("ESG", "流量计"),

	/**
	 * 视频站
	 */
	VIDEO("VIDEO", "视频站");


	@EnumValue
	@JsonValue
	private String value;

	private String label;

	public static String getLabel(String value) {
		for (SttpTypeEnum sttpType : values()) {
			if (sttpType.getValue().equals(value)) {
				return sttpType.getLabel();
			}
		}
		return null;
	}

	/**
	 * 解决前端传入为空 导致程序异常的问题
	 * 反序列化时，调用 @JsonCreator 标注的构造器或者工厂方法来创建对象
	 */
	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static SttpTypeEnum jsonCreator(@JsonProperty("value") String value) {
		if (!ObjectUtils.isEmpty(value)) {
			for (SttpTypeEnum item : values()) {
				if (item.value.equals(value)) {
					return item;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "SttpTypeEnum{" +
			"value='" + value + '\'' +
			", label='" + label + '\'' +
			'}';
	}
}
