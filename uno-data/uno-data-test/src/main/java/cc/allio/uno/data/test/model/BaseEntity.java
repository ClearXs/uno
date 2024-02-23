package cc.allio.uno.data.test.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author Chill
 */
@Data
public class BaseEntity implements Serializable {

	/**
	 * 主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@Id
	private Long id;

	/**
	 * 创建人
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createUser;

	/**
	 * 创建部门
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long createDept;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long updateUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 状态[0:未删除,1:删除]
	 */
	private Integer isDeleted;
}
