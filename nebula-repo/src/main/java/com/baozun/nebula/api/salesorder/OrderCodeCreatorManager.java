/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.api.salesorder;

/**
 * 订单Code创建接口
 * @author Tianlong.Zhang
 *
 */
public interface OrderCodeCreatorManager {
	
	/**
	 * 用于生成交易流水号，已废弃。请使用createOrderSerialNO();
	 * @return
	 */
	@Deprecated
	public String createOrderCode();
	
	/**
	 * 用于生成订单号
	 * @return
	 */
	public String createOrderCodeBySource(Integer source);
	
	/**
	 * 用于生成交易流水号
	 * @return
	 */
	public String createOrderSerialNO();
}
