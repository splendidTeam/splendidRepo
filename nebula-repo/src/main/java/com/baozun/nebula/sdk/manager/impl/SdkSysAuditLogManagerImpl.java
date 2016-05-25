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

import com.baozun.nebula.dao.system.SysAuditLogDao;
import com.baozun.nebula.model.system.SysAuditLog;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogManager;

/**
 * SysAuditLogManager
 * @author  xingyu.liu
 *
 */
@Transactional
@Service("sdkSysAuditLogManager") 
public class SdkSysAuditLogManagerImpl implements SdkSysAuditLogManager {

	@Autowired
	private SysAuditLogDao sysAuditLogDao;


	/**
	 * 保存SysAuditLog
	 * 
	 */
	public SysAuditLog saveSysAuditLog(SysAuditLog model){
	
		return sysAuditLogDao.save(model);
	}
	
	/**
	 * 通过id获取SysAuditLog
	 * 
	 */
	public SysAuditLog findSysAuditLogById(Long id){
	
		return sysAuditLogDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有SysAuditLog列表
	 * @return
	 */
	public List<SysAuditLog> findAllSysAuditLogList(){
	
		return sysAuditLogDao.findAllSysAuditLogList();
	};
	
	/**
	 * 通过ids获取SysAuditLog列表
	 * @param ids
	 * @return
	 */
	public List<SysAuditLog> findSysAuditLogListByIds(List<Long> ids){
	
		return sysAuditLogDao.findSysAuditLogListByIds(ids);
	};
	
	/**
	 * 通过参数map获取SysAuditLog列表
	 * @param paraMap
	 * @return
	 */
	public List<SysAuditLog> findSysAuditLogListByQueryMap(Map<String, Object> paraMap){
	
		return sysAuditLogDao.findSysAuditLogListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取SysAuditLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	public Pagination<SysAuditLog> findSysAuditLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return sysAuditLogDao.findSysAuditLogListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	public void deleteSysAuditLogListByQueryMap(Map<String, Object> paraMap) {
		
		sysAuditLogDao.deleteSysAuditLogListByQueryMap(paraMap);
	}
	
	
	
}
