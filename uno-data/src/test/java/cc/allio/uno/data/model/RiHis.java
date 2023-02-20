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

import cc.allio.uno.data.model.constant.IoTOperate;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 水位历史数据
 *
 * @author htz
 * @since 2021-12-15
 */
@EqualsAndHashCode(callSuper = true)
@TableName("wr_ri_his")
@Data
@ToString
public class RiHis extends RiReal {

	/**
	 * 操作状态
	 */
	@JsonSerialize(nullsUsing = NullSerializer.class)
	private IoTOperate operationStatus;
}
