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

import com.baozun.nebula.command.limit.LimitCommand;
import com.baozun.nebula.model.promotion.LimitHead;

/**
 * @author - 项硕
 */
public interface LimitHeadDao extends GenericEntityDao<LimitHead, Long> {

	/**
	 * 根据条件，分页查询限购列表
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	@NativeQuery(model = LimitCommand.class, pagable = true)
	Pagination<LimitCommand> findLimitCommandConditionallyWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> queryMap,@QueryParam("shopId") Long shopId);

	/**
	 * 根据条件，分页查询可编辑限购列表
	 * @param page
	 * @param sorts
	 * @param queryMap
	 * @return
	 */
	@NativeQuery(model = LimitCommand.class, pagable = true)
	Pagination<LimitCommand> findEditLimitCommandConditionallyWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> queryMap,@QueryParam("shopId") Long shopId);

	
	/**
	 * 根据ID查询限购VO
	 * @param id
	 * @return
	 */
	@NativeQuery(model = LimitCommand.class)
	LimitCommand findLimitCommandById(@QueryParam("id") Long id);

	/**
	 * 根据限购ID删除限购条件
	 * @param id
	 */
	@NativeUpdate
	void deleteLimitConditionByLimitId(@QueryParam("id") Long id);
	
	/**
	 * 查询与该限购时间上冲突的已启用的限购列表
	 * @param id
	 * @return
	 */
	@NativeQuery(model = LimitCommand.class)
	List<LimitCommand> findActiveLimitListByTimeScope(@QueryParam("id") Long id, @QueryParam("shopId") Long shopId);

	/**
	 * 查询已启用或已生效的限购列表
	 * @param now
	 * @return
	 */
	@NativeQuery(model = LimitCommand.class)
	List<LimitCommand> findActiveOrEffectiveLimitCommandList(@QueryParam("now") Date now);

	/**
	 * 更新限购生命周期
	 * @param lifecycleActive
	 */
	@NativeUpdate
	void updateLimitLifecycle(@QueryParam("id") Long id, @QueryParam("lifecycle") Integer lifecycle);

	/**
	 * 启用
	 * @param id
	 * @param lifecycleActive
	 */
	@NativeUpdate
	void activateLimit(@QueryParam("id") Long id, @QueryParam("userId") Long userId);
}


