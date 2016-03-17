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
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
  



import com.baozun.nebula.command.promotion.PromotionPriorityAdjustCommand; 
import com.baozun.nebula.model.promotion.PromotionPriorityAdjust;
 
public interface PromotionPriorityAdjustDao extends GenericEntityDao<PromotionPriorityAdjust, Long> {

	/**
	 * 根据促销id查询受益人群列表
	 * @param id
	 * @return
	 */ 
	@NativeQuery(model = PromotionPriorityAdjustCommand.class, pagable = true, withGroupby = true)
	public Pagination<PromotionPriorityAdjustCommand> findPriorityAdjustList(
			Page page, Sort[] sorts, @QueryParam Map<String, Object> queryMap);
	
	/*****
	 * 根据开始和结束时间查询优先级
	 * @param starttime
	 * @param endTime
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjust.class) 
	PromotionPriorityAdjust findPriorityAdjustByStartTimeAndEndTime
	(@QueryParam("startTime") Date starttime,@QueryParam("endTime") Date endTime);

	/**
	 * 查询所有非禁用的优先级列表
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustCommand.class)
	public List<PromotionPriorityAdjustCommand> findEffectivePriorityList(@QueryParam("shopId") Long shopId);
	
	@NativeUpdate
	Integer enableOrUnablePriorityById(@QueryParam("id")Long id, @QueryParam("userId")Long userId,
			@QueryParam("activeMark")Integer activeMark);
	
	@NativeQuery(model = PromotionPriorityAdjust.class) 
	PromotionPriorityAdjust findPriorityAdjustByName(@QueryParam("name") String name);
}


