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
package com.baozun.nebula.dao.payment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.PayInfoLog;
import com.baozun.nebula.sdk.command.PayInfoCommand;

/**
 * PayDetail 支付详情Dao
 * 
 * @author Tianlong.Zhang
 * 
 */
public interface PayInfoDao extends GenericEntityDao<PayInfo, Long> {
	
	@NativeQuery(model = PayInfoCommand.class)
	public List<PayInfoCommand> findPayInfoByOrderIds(@QueryParam("orderIds") List<Long> orderIds);

	/**
	 * 根据订单Id查询payInfoCommand
	 * @param orderId
	 * @return
	 */
	@NativeQuery(model = PayInfoCommand.class)
	List<PayInfoCommand> findPayInfoCommandByOrderId(@QueryParam("orderId") Long orderId);
	
	/**
	 * 通过参数map获取PayInfo列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PayInfo.class)
	List<PayInfo> findPayInfoListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
}
