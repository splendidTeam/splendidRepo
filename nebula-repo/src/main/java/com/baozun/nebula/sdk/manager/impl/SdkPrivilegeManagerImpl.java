package com.baozun.nebula.sdk.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.auth.PrivilegeCommand;
import com.baozun.nebula.dao.auth.OrgTypeDao;
import com.baozun.nebula.dao.auth.PrivilegeDao;
import com.baozun.nebula.dao.auth.PrivilegeUrlDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.sdk.manager.SdkPrivilegeManager;

@Service("sdkPrivilegeManager")
public class SdkPrivilegeManagerImpl implements SdkPrivilegeManager {
	public static final Logger log = LoggerFactory
			.getLogger(SdkPrivilegeManagerImpl.class);

	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Autowired
	private PrivilegeUrlDao privilegeUrlDao;
	
	@Autowired
	private OrgTypeDao orgTypeDao;

	@Override
	public Pagination<PrivilegeCommand> findPrivilegeCommandPageByQueryMap(Page page,Sort[] sorts,
			Map<String, Object> paraMap) {
		return privilegeDao.findPrivilegeCommandPageByQueryMap(page, sorts ,paraMap);
	}

	@Override
	public void removePrivilegeByIds(List<Long> ids) {
		//先删除关联
		privilegeUrlDao.removePrivilegeUrlBypIds(ids);
		privilegeDao.removePrivilegeByIds(ids);
	}

	@Override
	public void enableOrDisableById(Long id,int state) {
		privilegeDao.enableOrDisableById(id,state);
	}

	@Override
	public Privilege editPrivilege(PrivilegeCommand privilegeCommand) {
		Long id = privilegeCommand.getId();
		String acl = privilegeCommand.getAcl();
		check(id, acl);
		Long orgTypeId = privilegeCommand.getOrgTypeId();
		OrgType orgaType = orgTypeDao.getByPrimaryKey(orgTypeId);
		Privilege privilege = new Privilege();
		try {
			PropertyUtils.copyProperties(privilege, privilegeCommand);
		} catch (Exception e) {
			log.error("复制对象属性出错", e);
			throw new BusinessException("复制对象属性出错");
		}
		if(id != null){
			Privilege model = privilegeDao.getByPrimaryKey(id);
			model.setAcl(acl);
			model.setDescription(privilege.getDescription());
			model.setGroupName(privilege.getGroupName());
			model.setName(privilege.getName());
			model.setOrgType(orgaType);
			privilege = privilegeDao.save(model);
			//删除权限对应的url信息
			privilegeUrlDao.removePrivilegeUrlByParentId(id);
		}else{
			privilege.setOrgType(orgaType);
			privilege.setLifecycle(1);
			privilege = privilegeDao.save(privilege);
		}
		//插入权限url信息
		PrivilegeUrl[]  urls = privilegeCommand.getUrls();
		if(urls!=null && urls.length>0){
			for (PrivilegeUrl privilegeUrl : urls) {
				privilegeUrl.setPrivilege(privilege);
				privilegeUrlDao.save(privilegeUrl);
			}
		}
		return privilege;
	}
	
	private void check(Long id ,String acl){
		//不等空才验证
		Map<String, Object>  params = new HashMap<String, Object>();
		params.put("acl", acl);
		params.put("lifecycle", 1);
		List<Privilege> privileges =  privilegeDao.findPrivilegeListByQueryMap(params);
		if(id == null){
			if(privileges !=null && privileges.size()!=0){
				throw new BusinessException("权限编码已经存在");
			}
		}else{
			if(privileges !=null && privileges.size()!=0){
				for (Privilege privilege : privileges) {
					Long dbId = privilege.getId();
					if(!dbId.equals(id)){
						throw new BusinessException("权限编码已经存在");
					}
				}
			}	
		}
		
	}

	@Override
	public Privilege findPrivilegeById(Long id) {
		return privilegeDao.getByPrimaryKey(id);
	}
}
