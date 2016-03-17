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
import com.baozun.nebula.model.system.WarningConfig;

/**
 * WarningConfigManager
 * @author  何波
 *
 */
public interface SdkWarningConfigManager extends BaseManager{

	/**
	 * 保存WarningConfig
	 * 
	 */
	WarningConfig saveWarningConfig(WarningConfig model);
	
	/**
	 * 通过id获取WarningConfig
	 * 
	 */
	WarningConfig findWarningConfigById(Long id);
	
	/**
	 * 
	* @author 何波
	* @Description: 通过code查询
	* @param id
	* @return   
	* WarningConfig   
	* @throws
	 */
	WarningConfig findWarningConfigByCode(String code);

	/**
	 * 获取所有WarningConfig列表
	 * @return
	 */
	List<WarningConfig> findAllWarningConfigList();
	
	/**
	 * 通过ids获取WarningConfig列表
	 * @param ids
	 * @return
	 */
	List<WarningConfig> findWarningConfigListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取WarningConfig列表
	 * @param paraMap
	 * @return
	 */
	List<WarningConfig> findWarningConfigListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取WarningConfig列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<WarningConfig> findWarningConfigListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用WarningConfig
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableWarningConfigByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除WarningConfig
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeWarningConfigByIds(List<Long> ids);
	
	/**
	 * 获取有效的WarningConfig列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<WarningConfig> findAllEffectWarningConfigList();
	
	/**
	 * 通过参数map获取有效的WarningConfig列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<WarningConfig> findEffectWarningConfigListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的WarningConfig列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<WarningConfig> findEffectWarningConfigListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
}
