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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionPriorityAdjustGroupManager;

/**
 * PromotionPriorityAdjustGroupManager
 * @author  chenguang.zhou
 *
 */
@Transactional
@Service("promotionPriorityAdjustGroupManager") 
public class PromotionPriorityAdjustGroupManagerImpl implements PromotionPriorityAdjustGroupManager {

	@Autowired
	private SdkPromotionPriorityAdjustGroupManager sdkPromotionPriorityAdjustGroupManager;


	/**
	 * 保存PromotionPriorityAdjustGroup
	 * 
	 */
	public void savePromotionPriorityAdjustGroup(Long adjustId, String groupName, Long[] promotionIds,Integer excGroupType){
		PromotionPriorityAdjustGroup priorityAdjustGroup = null;
		for(Long promotionId : promotionIds){
			priorityAdjustGroup = new PromotionPriorityAdjustGroup();
			priorityAdjustGroup.setAdjustId(adjustId);
			priorityAdjustGroup.setGroupName(groupName);
			priorityAdjustGroup.setPromotionId(promotionId);
			priorityAdjustGroup.setGroupType(excGroupType);
			sdkPromotionPriorityAdjustGroupManager.savePromotionPriorityAdjustGroup(priorityAdjustGroup);
		}
	}


	@Override
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByAdjustId(Long adjustId) {
		return sdkPromotionPriorityAdjustGroupManager.findPromotionPriorityAdjustGroupListByAdjustId(adjustId);
	}


	@Override
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByGroupName(String groupName) {
		return sdkPromotionPriorityAdjustGroupManager.findPromotionPriorityAdjustGroupListByGroupName(groupName);
	}	
}
