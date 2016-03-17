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
package com.baozun.nebula.dao.salesorder;

import java.util.List;
import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.salesorder.OrderPromotion;
import com.baozun.nebula.sdk.command.OrderPromotionCommand;


/**
 * 订单行促销信息记录Dao
 * @author Tianlong.Zhang
 *
 */
public interface SdkOrderPromotionDao extends GenericEntityDao<OrderPromotion, Long>{

	/**
	 * type =1 标志订单总优惠信息  0为其他为行信息
	 * @param orderId
	 * @param type
	 * @return
	 */
	@NativeQuery(model=OrderPromotionCommand.class)
	public List<OrderPromotionCommand> findOrderProInfoByOrderId(@QueryParam("orderId")Long orderId,@QueryParam("type")Integer type);
	
	@NativeQuery(model=OrderPromotionCommand.class)
	public List<OrderPromotionCommand> findOrderProInfosByOrderLineIds(@QueryParam("orderLineIds")List<Long> orderLineIds);
	
	@NativeQuery(model=OrderPromotionCommand.class)
	public List<OrderPromotionCommand> findOrderProsInfoByOrderId(@QueryParam("orderId")Long orderId);
}
