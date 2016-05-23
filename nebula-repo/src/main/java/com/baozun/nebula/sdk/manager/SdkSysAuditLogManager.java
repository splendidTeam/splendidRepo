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
import com.baozun.nebula.model.system.SysAuditLog;

/**
 * SysAuditLogManager
 * @author  xingyu.liu
 *
 */
public interface SdkSysAuditLogManager {

	/**
	 * 保存SysAuditLog
	 * 
	 */
	SysAuditLog saveSysAuditLog(SysAuditLog model);
	
	/**
	 * 通过id获取SysAuditLog
	 * 
	 */
	SysAuditLog findSysAuditLogById(Long id);

	/**
	 * 获取所有SysAuditLog列表
	 * @return
	 */
	List<SysAuditLog> findAllSysAuditLogList();
	
	/**
	 * 通过ids获取SysAuditLog列表
	 * @param ids
	 * @return
	 */
	List<SysAuditLog> findSysAuditLogListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取SysAuditLog列表
	 * @param paraMap
	 * @return
	 */
	List<SysAuditLog> findSysAuditLogListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取SysAuditLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<SysAuditLog> findSysAuditLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 归档一段时间的记录，则需要删除该表的记录
	 * @param paraMap
	 */
	void deleteSysAuditLogListByQueryMap(Map<String, Object> paraMap);
	
	
	
}
