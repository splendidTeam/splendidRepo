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

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.command.promotion.ConditionNormalCommand;
import com.baozun.nebula.model.promotion.PromotionConditionNormal;

/**
 * @author - 项硕
 */
public interface PromotionConditionDao extends GenericEntityDao<PromotionConditionNormal, Long> {

	/**
	 * 根据促销id查询条件列表
	 * @param id
	 * @return
	 */
	@NativeQuery(model = ConditionNormalCommand.class)
	ConditionNormalCommand findByPromotionId(@QueryParam("promotionId") Long id);

	/**
	 * 获取所有有效优惠条件列表
	 */
	@NativeQuery(model = PromotionConditionNormal.class)
	public List<PromotionConditionNormal> findByTime(@QueryParam("time")Date date);

	@NativeUpdate
	void deleteByPromotionId(@QueryParam("promotionId") Long id);

	@NativeQuery(model = PromotionConditionNormal.class)
	PromotionConditionNormal findPromotionConditionNormalByPromotionId(
			@QueryParam("promotionId") Long fromPromotionId);
}


