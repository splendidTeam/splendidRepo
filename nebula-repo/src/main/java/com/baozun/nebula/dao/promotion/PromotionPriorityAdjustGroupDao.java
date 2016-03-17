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

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;

/**
 * PromotionPriorityAdjustGroupDao
 * @author  chenguang.zhou
 *
 */
public interface PromotionPriorityAdjustGroupDao extends GenericEntityDao<PromotionPriorityAdjustGroup,Long>{

	/**
	 * 获取所有PromotionPriorityAdjustGroup列表
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findAllPromotionPriorityAdjustGroupList();
	
	/**
	 * 通过ids获取PromotionPriorityAdjustGroup列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取PromotionPriorityAdjustGroup列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取PromotionPriorityAdjustGroup列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	Pagination<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用PromotionPriorityAdjustGroup
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisablePromotionPriorityAdjustGroupByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除PromotionPriorityAdjustGroup
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removePromotionPriorityAdjustGroupByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 
	 * 物理删除
	 * @param adjustId
	 * @param groupName
	 */
	@NativeUpdate
	Integer removePromotionPriorityAdjustGroupByAdjustIdsAndGroupName(@QueryParam("adjustId")Long adjustId,@QueryParam("groupName")String groupName);
	
	/**
	 * 获取有效的PromotionPriorityAdjustGroup列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findAllEffectPromotionPriorityAdjustGroupList();
	
	/**
	 * 通过参数map获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	Pagination<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过adjustId获取PromotionPriorityAdjustGroup集合
	 * @param adjustId
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByAdjustId(@QueryParam("adjustId") Long adjustId);
	
	/**
	 * 通过groupName获取PromotionPriorityAdjustGroup集合
	 * @param adjustId
	 * @return
	 */
	@NativeQuery(model = PromotionPriorityAdjustGroup.class)
	List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByGroupName(@QueryParam("groupName") String groupName);
}
