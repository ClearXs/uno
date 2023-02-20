package cc.allio.uno.data.model;

import cc.allio.uno.data.model.constant.AlarmStatusEnum;
import cc.allio.uno.data.model.constant.StinfoStatusEnum;
import cc.allio.uno.data.model.constant.SttpTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * 测站基本实时数据
 *
 * @author jiangwei
 * @date 2021/12/22 00:48
 * @since 1.0
 */
@Data
public class StationReal extends BaseEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 测站编码
	 */
	@JsonSerialize(using = ToStringSerializer.class, nullsUsing = NullSerializer.class)
	private String stcd;
	/**
	 * 测站ID
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private Long stId;

	/**
	 * 测站类型
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private SttpTypeEnum sttp;

	/**
	 * 监测时间
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private Date mot;

	/**
	 * 报警状态
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private AlarmStatusEnum alarmStatus;

	/**
	 * 通讯状态
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private StinfoStatusEnum commStatus;
}
