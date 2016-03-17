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

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.PrivilegeUrl;

/**
 * PrivilegeUrlManager
 * @author  何波
 *
 */
public interface SdkPrivilegeUrlManager extends BaseManager{

	/**
	 * 保存PrivilegeUrl
	 * 
	 */
	PrivilegeUrl savePrivilegeUrl(PrivilegeUrl model);
	
	/**
	 * 通过id获取PrivilegeUrl
	 * 
	 */
	PrivilegeUrl findPrivilegeUrlById(Long id);

	/**
	 * 获取所有PrivilegeUrl列表
	 * @return
	 */
	List<PrivilegeUrl> findAllPrivilegeUrlList();
	
	/**
	 * 通过ids获取PrivilegeUrl列表
	 * @param ids
	 * @return
	 */
	List<PrivilegeUrl> findPrivilegeUrlListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取PrivilegeUrl列表
	 * @param paraMap
	 * @return
	 */
	List<PrivilegeUrl> findPrivilegeUrlListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取PrivilegeUrl列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<PrivilegeUrl> findPrivilegeUrlListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量删除PrivilegeUrl
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removePrivilegeUrlByIds(List<Long> ids);
	
	
}
