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

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.auth.PrivilegeUrlDao;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.sdk.manager.SdkPrivilegeUrlManager;

/**
 * PrivilegeUrlManager
 * @author  何波
 *
 */
@Transactional
@Service("sdkPrivilegeUrlManager") 
public class SdkPrivilegeUrlManagerImpl implements SdkPrivilegeUrlManager {

	@Autowired
	private PrivilegeUrlDao privilegeUrlDao;


	/**
	 * 保存PrivilegeUrl
	 * 
	 */
	public PrivilegeUrl savePrivilegeUrl(PrivilegeUrl model){
	
		return privilegeUrlDao.save(model);
	}
	
	/**
	 * 通过id获取PrivilegeUrl
	 * 
	 */
	public PrivilegeUrl findPrivilegeUrlById(Long id){
	
		return privilegeUrlDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有PrivilegeUrl列表
	 * @return
	 */
	public List<PrivilegeUrl> findAllPrivilegeUrlList(){
	
		return privilegeUrlDao.findAllPrivilegeUrlList();
	};
	
	/**
	 * 通过ids获取PrivilegeUrl列表
	 * @param ids
	 * @return
	 */
	public List<PrivilegeUrl> findPrivilegeUrlListByIds(List<Long> ids){
	
		return privilegeUrlDao.findPrivilegeUrlListByIds(ids);
	};
	
	/**
	 * 通过参数map获取PrivilegeUrl列表
	 * @param paraMap
	 * @return
	 */
	public List<PrivilegeUrl> findPrivilegeUrlListByQueryMap(Map<String, Object> paraMap){
	
		return privilegeUrlDao.findPrivilegeUrlListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取PrivilegeUrl列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	public Pagination<PrivilegeUrl> findPrivilegeUrlListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return privilegeUrlDao.findPrivilegeUrlListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	
	/**
	 * 通过ids批量删除PrivilegeUrl
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removePrivilegeUrlByIds(List<Long> ids){
		privilegeUrlDao.removePrivilegeUrlByIds(ids);
	}
	
	
}
