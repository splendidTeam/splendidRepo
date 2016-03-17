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
package com.baozun.nebula.sdk.manager.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.model.promotion.PromotionPriorityAdjustGroup;
import com.baozun.nebula.sdk.manager.SdkPromotionPriorityAdjustGroupManager;
import com.baozun.nebula.dao.promotion.PromotionPriorityAdjustGroupDao;

/**
 * PromotionPriorityAdjustGroupManager
 * @author  chenguang.zhou
 *
 */
@Transactional
@Service("sdkPromotionPriorityAdjustGroupManager") 
public class SdkPromotionPriorityAdjustGroupManagerImpl implements SdkPromotionPriorityAdjustGroupManager {

	@Autowired
	private PromotionPriorityAdjustGroupDao promotionPriorityAdjustGroupDao;


	/**
	 * 保存PromotionPriorityAdjustGroup
	 * 
	 */
	public PromotionPriorityAdjustGroup savePromotionPriorityAdjustGroup(PromotionPriorityAdjustGroup model){
	
		return promotionPriorityAdjustGroupDao.save(model);
	}
	
	/**
	 * 通过id获取PromotionPriorityAdjustGroup
	 * 
	 */
	@Transactional(readOnly=true)
	public PromotionPriorityAdjustGroup findPromotionPriorityAdjustGroupById(Long id){
	
		return promotionPriorityAdjustGroupDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有PromotionPriorityAdjustGroup列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findAllPromotionPriorityAdjustGroupList(){
	
		return promotionPriorityAdjustGroupDao.findAllPromotionPriorityAdjustGroupList();
	};
	
	/**
	 * 通过ids获取PromotionPriorityAdjustGroup列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByIds(List<Long> ids){
	
		return promotionPriorityAdjustGroupDao.findPromotionPriorityAdjustGroupListByIds(ids);
	};
	
	/**
	 * 通过参数map获取PromotionPriorityAdjustGroup列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMap(Map<String, Object> paraMap){
	
		return promotionPriorityAdjustGroupDao.findPromotionPriorityAdjustGroupListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取PromotionPriorityAdjustGroup列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return promotionPriorityAdjustGroupDao.findPromotionPriorityAdjustGroupListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用PromotionPriorityAdjustGroup
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisablePromotionPriorityAdjustGroupByIds(List<Long> ids,Integer state){
		promotionPriorityAdjustGroupDao.enableOrDisablePromotionPriorityAdjustGroupByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除PromotionPriorityAdjustGroup
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removePromotionPriorityAdjustGroupByIds(List<Long> ids){
		promotionPriorityAdjustGroupDao.removePromotionPriorityAdjustGroupByIds(ids);
	}
	
	
	/**
	 * 获取有效的PromotionPriorityAdjustGroup列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findAllEffectPromotionPriorityAdjustGroupList(){
	
		return promotionPriorityAdjustGroupDao.findAllEffectPromotionPriorityAdjustGroupList();
	};
	
	/**
	 * 通过参数map获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMap(Map<String, Object> paraMap){
	
		return promotionPriorityAdjustGroupDao.findEffectPromotionPriorityAdjustGroupListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的PromotionPriorityAdjustGroup列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<PromotionPriorityAdjustGroup> findEffectPromotionPriorityAdjustGroupListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return promotionPriorityAdjustGroupDao.findEffectPromotionPriorityAdjustGroupListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByAdjustId(Long adjustId) {
		return promotionPriorityAdjustGroupDao.findPromotionPriorityAdjustGroupListByAdjustId(adjustId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<PromotionPriorityAdjustGroup> findPromotionPriorityAdjustGroupListByGroupName(String groupName) {
		return promotionPriorityAdjustGroupDao.findPromotionPriorityAdjustGroupListByGroupName(groupName);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer removePromotionPriorityAdjustGroupByAdjustIdsAndGroupName(
			Long adjustId, String groupName) {
		return promotionPriorityAdjustGroupDao.removePromotionPriorityAdjustGroupByAdjustIdsAndGroupName(adjustId,groupName);
	}
	
	
}
