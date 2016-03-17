/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.wormhole.scm.handler;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;

/**
 * @author yfxie
 *
 */
public interface SyncSalesOrderHandler {
	
	/**
	 * 同步oms推送过来的订单状态
	 * 
	 * @param orderSV5List
	 */
	public boolean syncSoStatus(List<OrderStatusV5> orderSV5List);
	/**
	 * 根据订单号查询物流方式
	 * @param orderCode
	 * @return
	 */
	public String getLogisticsProviderCode(String orderCode);
	
}
