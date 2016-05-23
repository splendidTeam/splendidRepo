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

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.utils.PropertyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.SysAuditLogDao;
import com.baozun.nebula.dao.system.SysAuditLogHistoryDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.system.SysAuditLog;
import com.baozun.nebula.model.system.SysAuditLogHistory;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogHistoryManager;
import com.feilong.core.Validator;

/**
 * SysAuditLogHistoryManager
 * @author  xingyu.liu
 *
 */
@Transactional
@Service("sdkSysAuditLogHistoryManager") 
public class SdkSysAuditLogHistoryManagerImpl implements SdkSysAuditLogHistoryManager {

	@Autowired
	private SysAuditLogHistoryDao sysAuditLogHistoryDao;
	
	@Autowired
	private SysAuditLogDao sysAuditLogDao;


	/**
	 * 保存SysAuditLogHistory
	 * 
	 */
	public SysAuditLogHistory saveSysAuditLogHistory(SysAuditLogHistory model){
	
		return sysAuditLogHistoryDao.save(model);
	}
	
	/**
	 * 通过id获取SysAuditLogHistory
	 * 
	 */
	public SysAuditLogHistory findSysAuditLogHistoryById(Long id){
	
		return sysAuditLogHistoryDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有SysAuditLogHistory列表
	 * @return
	 */
	public List<SysAuditLogHistory> findAllSysAuditLogHistoryList(){
	
		return sysAuditLogHistoryDao.findAllSysAuditLogHistoryList();
	};
	
	/**
	 * 通过ids获取SysAuditLogHistory列表
	 * @param ids
	 * @return
	 */
	public List<SysAuditLogHistory> findSysAuditLogHistoryListByIds(List<Long> ids){
	
		return sysAuditLogHistoryDao.findSysAuditLogHistoryListByIds(ids);
	};
	
	/**
	 * 通过参数map获取SysAuditLogHistory列表
	 * @param paraMap
	 * @return
	 */
	public List<SysAuditLogHistory> findSysAuditLogHistoryListByQueryMap(Map<String, Object> paraMap){
	
		return sysAuditLogHistoryDao.findSysAuditLogHistoryListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取SysAuditLogHistory列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	public Pagination<SysAuditLogHistory> findSysAuditLogHistoryListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return sysAuditLogHistoryDao.findSysAuditLogHistoryListByQueryMapWithPage(page,sorts,paraMap);
	}

    /**
     * 日志归档
     */
	@Override
	public void archive() {
		List<SysAuditLog> sysAuditLogs = sysAuditLogDao.findAllSysAuditLogList();
		if(Validator.isNotNullOrEmpty(sysAuditLogs)){
			try {
				for(SysAuditLog sysAuditLog :sysAuditLogs){
					SysAuditLogHistory sysAuditLogHistory = new SysAuditLogHistory();
					PropertyUtil.copyProperties(sysAuditLog, sysAuditLogHistory);
					sysAuditLogHistory.setArchiveTime(new Date());
					sysAuditLogHistoryDao.save(sysAuditLogHistory);
				}
				sysAuditLogDao.deleteSysAuditLogListByQueryMap(null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new BusinessException(ErrorCodes.SYSTEM_ERROR);
			}
		}
	};
	
}
