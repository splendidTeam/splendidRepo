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

package com.baozun.nebula.dao.promotion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.promotion.PromotionPriorityAdjustDetailCommand;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustDetail;

public interface PromotionPriorityAdjustDetailDao extends GenericEntityDao<PromotionPriorityAdjustDetail, Long> {
	/***
	 * 根据条件查询优先级详情表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustDetailCommand.class)
	List<PromotionPriorityAdjustDetailCommand> findPriorityAdjustDetailByAdjustId(@QueryParam Map<String, Object> paraMap);

	/**
	 * 删除当前adjustid所属优先级详情条目
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeUpdate
	Integer deletePromotionPriorityAdjustDetailByAdjustid(@QueryParam("adjustid") Long adjustid);

	/**
	 * 根据当前adjustid优先级详情表
	 * 
	 * @param itemId
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustDetailCommand.class)
	List<PromotionPriorityAdjustDetailCommand> findPromotionPriorityAdjustDetailByAdjustid(@QueryParam("adjustid") Long adjustid);

	/**
	 * 根据优先级ID查询详细列表
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustDetailCommand.class)
	List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByPriorityId(@QueryParam("priorityId") Long id);

	/**
	 * 根据ShopID，和CurrentTime获取优先级调整列表
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustDetailCommand.class)
	List<PromotionPriorityAdjustDetailCommand> findPriorityDetailListByShopIdCurrentTime(@QueryParam("shopIds") List<Long> shopIds);
}
