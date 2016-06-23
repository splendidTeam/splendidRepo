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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.system.SysAuditLogCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.system.SysAuditLogDao;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.system.SysAuditLog;
import com.baozun.nebula.sdk.manager.SdkSysAuditLogManager;
import com.feilong.core.Validator;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * SysAuditLogManager
 * @author  xingyu.liu
 *
 */
@Transactional
@Service("sdkSysAuditLogManager") 
public class SdkSysAuditLogManagerImpl implements SdkSysAuditLogManager {

	@Autowired
	private SysAuditLogDao 							sysAuditLogDao;
	
	@Autowired
	private UserDao									userDao;


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
	public Pagination<SysAuditLogCommand> findSysAuditLogListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		
		Pagination<SysAuditLogCommand> result =sysAuditLogDao.findSysAuditLogListByQueryMapWithPage(page,sorts,paraMap);
		
		//1.调整请求参数显示格式
		//2.设置操作人显示名
		if(Validator.isNotNullOrEmpty(result)&&
				Validator.isNotNullOrEmpty(result.getItems())){
			List<Long> backUserIds =new ArrayList<Long>();
			for(SysAuditLogCommand auditLogCommand : result.getItems()){
				if(Validator.isNotNullOrEmpty(auditLogCommand.getOperaterId())){
					backUserIds.add(auditLogCommand.getOperaterId());
				}
			}
			Map<Long, User> backIdAndInfoMap =new HashMap<Long, User>();
			if(Validator.isNotNullOrEmpty(backUserIds)){
				List<User> userList=userDao.findUserListByIds(backUserIds);
				for (User user : userList) {
					backIdAndInfoMap.put(user.getId(), user);
				}
			}
			String formatPara ="";
			String formatValue ="";
			for(SysAuditLogCommand auditLogCommand : result.getItems()){
				if(Validator.isNotNullOrEmpty(auditLogCommand.getParameters())&&
						auditLogCommand.getParameters() != "{}"){
					formatPara ="";
					formatValue ="";
					Map<String, Object> map = JsonUtil.toMap(auditLogCommand.getParameters());
					int i =0;
					for (Map.Entry<String, Object> entry : map.entrySet()) {
						formatValue =entry.getValue().toString();
						//请求参数值去头、去尾，例如["26,black"]->26,black
						formatPara += entry.getKey() + " : " + formatValue.substring(2 , formatValue.length()-2);
						i++;
						if(i != map.size()){
							formatPara +="</br>";
						}
					}
					auditLogCommand.setParametersLabel(formatPara);
				}else{
					auditLogCommand.setParametersLabel("");
				}
				if(Validator.isNotNullOrEmpty(backIdAndInfoMap)&&
						Validator.isNotNullOrEmpty(backIdAndInfoMap
								.get(auditLogCommand.getOperaterId()))){
					auditLogCommand.setOperatorLabel(backIdAndInfoMap
							.get(auditLogCommand.getOperaterId()).getUserName());
				}
				
			}
		}
		return result;
	}

	@Override
	public void deleteSysAuditLogListByQueryMap(Map<String, Object> paraMap) {
		
		sysAuditLogDao.deleteSysAuditLogListByQueryMap(paraMap);
	}
	
	
	
}
