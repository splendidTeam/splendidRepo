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

import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.command.promotion.PromotionQueryCommand;
import com.baozun.nebula.model.promotion.PromotionHead;

/**
 * @author - 项硕
 */
public interface PromotionDao extends GenericEntityDao<PromotionHead, Long> {

	/**
	 * 根据条件分页查询待启用的促销列表（包含步骤不完整的）
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionQueryCommand.class, pagable = true, withGroupby = true)
	public Pagination<PromotionQueryCommand> findInactivePromotionListConditionallyWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 根据条件分页查询步骤完整的促销列表
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionQueryCommand.class, pagable = true, withGroupby = true)
	public Pagination<PromotionQueryCommand> findCompletePromotionListConditionallyWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 根据id查询促销
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PromotionCommand.class)
	public PromotionCommand findPromotionById(@QueryParam("promotionId") Long id);

	/**
	 * 根据id更新促销生命周期
	 * 
	 * @param id
	 * @param lifecycle
	 */
	@NativeUpdate
	public Integer updateLifecycle(@QueryParam("id") Long id, @QueryParam("lifecycle") Integer lifecycle);

	/**
	 * 根据名称查询促销
	 * 
	 * @param name
	 * @return
	 */
	@NativeQuery(model = PromotionHead.class)
	public PromotionHead findPromotionByName(@QueryParam("name") String name);

	/**
	 * 根据时间点获取冲突的促销
	 * 
	 * @param timePoint
	 * @param shopId
	 * @return
	 */
	@NativeQuery(model = PromotionCommand.class)
	List<PromotionCommand> findConflictingPromotionListByTimePoint(@QueryParam("timePoint") Date timePoint, @QueryParam("shopId") Long shopId);

	/**
	 * 获得所有活动有效期包含在当前时间的以启用的活动
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionCommand.class)
	List<PromotionCommand> findPromotionEnableList(@QueryParam("currentTime") Date currentTime);
	
	/**
	 * 获得所有已经启用的活动
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionCommand.class)
	List<PromotionCommand> findAllPromotionEnableList();

	/**
	 * 根据启动时间和活动id查询活动
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionHead.class)
	PromotionHead checkPromtionTimeEffectiveByID(@QueryParam("pId") Long pId, @QueryParam("currentTime") Date currentTime);

	@NativeQuery(model = PromotionHead.class)
	public PromotionHead findPromotionHeadById(@QueryParam("id") Long id);

	/**
	 * 根据条件ID更新促销优先级
	 * 
	 * @param conditionId
	 * @param priority
	 */
	@NativeUpdate
	public void updatePriorityByConditionId(@QueryParam("conditionId") Long conditionId, @QueryParam("priority") int priority);

	/**
	 * 查询所有在时间上与之冲突的有效促销列表
	 * 
	 * @param id
	 * @param shopId
	 * @return
	 */
	@NativeQuery(model = PromotionQueryCommand.class)
	public List<PromotionQueryCommand> findActivePromotionListByTimeScope(@QueryParam("id") Long id, @QueryParam("shopId") Long shopId);

	/**
	 * 根据ID查询促销VO
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PromotionQueryCommand.class)
	public PromotionQueryCommand findPromotionQueryCommandById(@QueryParam("id") Long id);

	/**
	 * 启用促销
	 * 
	 * @param id
	 * @param userId
	 */
	@NativeUpdate
	public void activatePromotionById(@QueryParam("id") Long id, @QueryParam("userId") Long userId);

	/**
	 * 取消促销
	 * 
	 * @param id
	 * @param userId
	 */
	@NativeUpdate
	public void inactivatePromotionById(@QueryParam("id") Long id, @QueryParam("userId") Long userId);
}
