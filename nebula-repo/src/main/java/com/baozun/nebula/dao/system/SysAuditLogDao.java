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
package com.baozun.nebula.dao.system;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.command.system.SysAuditLogCommand;
import com.baozun.nebula.model.system.SysAuditLog;

/**
 * SysAuditLogDao
 * @author  xingyu.liu
 *
 */
public interface SysAuditLogDao extends GenericEntityDao<SysAuditLog,Long>{

	/**
	 * 获取所有SysAuditLog列表
	 * @return
	 */
	@NativeQuery(model = SysAuditLog.class)
	List<SysAuditLog> findAllSysAuditLogList();
	
	/**
	 * 通过ids获取SysAuditLog列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = SysAuditLog.class)
	List<SysAuditLog> findSysAuditLogListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取SysAuditLog列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = SysAuditLog.class)
	List<SysAuditLog> findSysAuditLogListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取SysAuditLog列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = SysAuditLogCommand.class)
	Pagination<SysAuditLogCommand> findSysAuditLogListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 归档一段时间的记录，则需要删除该表的记录
	 * @param paraMap
	 */
	@NativeUpdate
	void deleteSysAuditLogListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
}
