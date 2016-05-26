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
package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;

/**
 * PromotionPriorityAdjustGroupManager
 * @author  chenguang.zhou
 *
 */
public interface SdkPromotionPriorityAdjustGroupManager extends BaseManager{

	/**
	 * 保存PromotionPriorityAdjustGroup
	 * 
	 */
	PromotionPriorityAdjustGroup savePromotionPriorityAdjustGroup(PromotionPriorityAdjustGroup model);
	
	/**
	 * 通过id获取PromotionPriorityAdjustGroup
	 * 
	 */
	PromotionPriorityAdjustGroup findPromotionPriorityAdjustGroupById(Long id);

	/**
	 * 获取所有PromotionPriorityAdjustGroup列表
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findAllPromotionPriorityAdjustGroupList();
	
	/**
	 * 通过ids获取PromotionPriorityAdjustGroup列表
	 * @param ids
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取PromotionPriorityAdjustGroup列表
	 * @param paraMap
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取PromotionPriorityAdjustGroup列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用PromotionPriorityAdjustGroup
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisablePromotionPriorityAdjustGroupByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除PromotionPriorityAdjustGroup
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removePromotionPriorityAdjustGroupByIds(List<Long> ids);
	
	/**
	 * 根据优先级id和分组名物理删除数据
	 * @param adjustId
	 * @param groupName
	 */
	Integer removePromotionPriorityAdjustGroupByAdjustIdsAndGroupName(Long adjustId,String groupName);
	
	/**
	 * 获取有效的PromotionPriorityAdjustGroup列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findAllEffectPromotionPriorityAdjustGroupList();
	
	/**
	 * 通过参数map获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
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
