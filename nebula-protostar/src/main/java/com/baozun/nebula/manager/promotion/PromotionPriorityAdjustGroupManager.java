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
package com.baozun.nebula.manager.promotion;

import java.util.List;

import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;

/**
 * PromotionPriorityAdjustGroupManager
 * @author  chenguang.zhou
 *
 */
public interface PromotionPriorityAdjustGroupManager {

	/**
	 * 保存PromotionPriorityAdjustGroup
	 * 
	 */
	public void savePromotionPriorityAdjustGroup(Long adjustId, String groupName, Long[] promotionIds,Integer excGroupType);
	
	/**
	 * 通过adjustId获取PromotionPriorityAdjustGroup集合
	 * 
	 */
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByAdjustId(Long adjustId);
	
	
	/**
	 * 通过groupName获取PromotionPriorityAdjustGroup集合
	 * @param groupName
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByGroupName(String groupName);

}
