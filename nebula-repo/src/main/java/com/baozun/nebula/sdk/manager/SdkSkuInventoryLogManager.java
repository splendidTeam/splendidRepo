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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.SkuInventoryLog;

/**
 * SkuInventoryLogManager
 * @author  何波
 *
 */
public interface SdkSkuInventoryLogManager extends BaseManager{

	/**
	 * 保存SkuInventoryLog
	 * 
	 */
	SkuInventoryLog saveSkuInventoryLog(SkuInventoryLog model);
	
	/**
	 * 通过id获取SkuInventoryLog
	 * 
	 */
	SkuInventoryLog findSkuInventoryLogById(Long id);

	/**
	 * 获取所有SkuInventoryLog列表
	 * @return
	 */
	List<SkuInventoryLog> findAllSkuInventoryLogList();
	
	/**
	 * 通过ids获取SkuInventoryLog列表
	 * @param ids
	 * @return
	 */
	List<SkuInventoryLog> findSkuInventoryLogListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取SkuInventoryLog列表
	 * @param paraMap
	 * @return
	 */
	List<SkuInventoryLog> findSkuInventoryLogListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取SkuInventoryLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<SkuInventoryLog> findSkuInventoryLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用SkuInventoryLog
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableSkuInventoryLogByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除SkuInventoryLog
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeSkuInventoryLogByIds(List<Long> ids);
	
	/**
	 * 获取有效的SkuInventoryLog列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<SkuInventoryLog> findAllEffectSkuInventoryLogList();
	
	/**
	 * 通过参数map获取有效的SkuInventoryLog列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<SkuInventoryLog> findEffectSkuInventoryLogListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的SkuInventoryLog列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<SkuInventoryLog> findEffectSkuInventoryLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
}
