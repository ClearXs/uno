/*
 *      Copyright (c) 2018-2028, www.beree.com.cn All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the beree.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 */
package cc.allio.uno.data.model;

import cc.allio.uno.data.model.constant.WaterPotentialEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水位实时数据
 *
 * @author htz
 * @since 2021-12-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("wr_ri_real")
public class RiReal extends StationReal implements Serializable {

	/**
	 * 水位
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private BigDecimal z;

	/**
	 * 埋深
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private BigDecimal d;

	/**
	 * 水势
	 */
	private WaterPotentialEnum wptn = WaterPotentialEnum.KEEP;

}
