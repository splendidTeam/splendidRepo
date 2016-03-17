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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.rule.CustomizeFilterClassCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.rule.CustomizeFilterClass;

/**
 * CustomizeFilterClassManager
 * @author  lxy
 *
 */
public interface SdkCustomizeFilterClassManager extends BaseManager{

	/**
	 * 保存CustomizeFilterClass
	 * 
	 */
	CustomizeFilterClass saveCustomizeFilterClass(CustomizeFilterClass model);
	
	/**
	 * 通过id获取CustomizeFilterClass
	 * 
	 */
	CustomizeFilterClass findCustomizeFilterClassById(Long id);

	/**
	 * 获取所有CustomizeFilterClass列表
	 * @return
	 */
	List<CustomizeFilterClass> findAllCustomizeFilterClassList();
	
	/**
	 * 通过ids获取CustomizeFilterClass列表
	 * @param ids
	 * @return
	 */
	List<CustomizeFilterClass> findCustomizeFilterClassListByIds(List<Long> ids);
	
	CustomizeFilterClass findCustomizeFilterClassListByServiceName(String svcName);
	
	/**
	 * 通过参数map获取CustomizeFilterClass列表
	 * @param paraMap
	 * @return
	 */
	List<CustomizeFilterClass> findCustomizeFilterClassListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CustomizeFilterClass列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 根据店铺id分页获取CustomizeFilterClass列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapAndShopIdWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap ,Long shopId);
	
	/**
	 * 通过ids批量启用或禁用CustomizeFilterClass
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableCustomizeFilterClassByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除CustomizeFilterClass
	 * 设置lifecycle = 2
	 * @param ids
	 * @return
	 */
	void removeCustomizeFilterClassByIds(List<Long> ids);
	
	/**
	 * 获取有效的CustomizeFilterClass列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<CustomizeFilterClass> findAllEffectCustomizeFilterClassList();
	
	/**
	 * 通过参数map获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	/**新增/更新自定义筛选器
	 * @param customizeFilterClass
	 */
	void createOrUpdate(CustomizeFilterClass customizeFilterClass);
	
	/**
	 * 通过ID更新缓存版本
	 * @param id
	 */
	public void updateCacheVersion(Long id);
	
	/**
	 * 通过scope type和shopId获取有效的CustomizeFilterClass列表
	 * @param type
	 * @return
	 */
	public List<CustomizeFilterClass> findEffectCustomizeFilterClassListByTypeAndShopId(Integer type, Long shopId);
	
	
}
