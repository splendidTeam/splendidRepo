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
package com.baozun.nebula.manager.baseinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MenuCommand;
import com.baozun.nebula.command.MenuQueryCommand;
import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.dao.auth.OrgTypeDao;
import com.baozun.nebula.dao.auth.PrivilegeDao;
import com.baozun.nebula.dao.auth.PrivilegeUrlDao;
import com.baozun.nebula.dao.auth.RolePrivilegeDao;
import com.baozun.nebula.dao.baseinfo.MenuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.auth.RoleManager;
import com.baozun.nebula.model.auth.OrgType;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.PrivilegeUrl;
import com.baozun.nebula.model.baseinfo.Menu;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.web.UserDetails;

/**
 * 菜单
 * 
 * @author dianchao.song
 */
@Service
@Transactional
public class MenuManagerImpl implements MenuManager{

	private static final Logger	log	= LoggerFactory.getLogger(MenuManagerImpl.class);

	@Autowired
	private MenuDao	menuDao;
	@Autowired
	private PrivilegeDao privilegeDao;
	@Autowired
	private PrivilegeUrlDao privilegeUrlDao;
	@Autowired
	private OrgTypeDao orgTypeDao;
	@Autowired
	private RoleManager	roleManager;
	@Autowired
	private RolePrivilegeDao rolePrivilegeDao;
	

	/*
	 * (non-Javadoc)
	 * @see com.baozun.nebula.manager.baseinfo.MenuManager#getUserMenu(com.baozun.nebula.web.UserDetails)
	 */
	@Transactional(readOnly = true)
	public List<MenuCommand> getUserMenu(UserDetails userDetails){
		Long userId = userDetails.getUserId();
		Long currentOrganizationId = userDetails.getCurrentOrganizationId();

		List<MenuCommand> userMenus = menuDao.findUserMenu(userId, currentOrganizationId);

		List<MenuCommand> menuCommandList = new ArrayList<MenuCommand>();
		for (MenuCommand menuCommand : userMenus){
			boolean mark = false;
			for (MenuCommand menu2 : userMenus){
				if (menuCommand.getParentId() != null && menuCommand.getParentId().equals(menu2.getId())){
					mark = true;
					if (menu2.getChildren() == null){
						menu2.setChildren(new ArrayList<MenuCommand>());
					}
					menu2.getChildren().add(menuCommand);
					break;
				}
			}
			if (!mark){
				menuCommandList.add(menuCommand);
			}
		}

		return menuCommandList;
	}

	@Override
	public List<MenuCommand> getAllMenus() {
		
		return menuDao.getAllMenus();
	}

	@Override
	public MenuQueryCommand getMenuByUrl( Map<String, Object> paraMap) {
		return menuDao.getMenuByUrl(paraMap);
	}

	@Override
	public Menu edit(MenuQueryCommand model) {
		Long id = model.getId();
		//验证url
		String acl = model.getAcl();
		if(acl== null || acl.equals("")){
			throw new BusinessException("权限编码为空");
		}
		Menu menu = null;
		if(id == null){
			check(id, model.getUrl(),model.getAcl(),null);
			//菜单信息
			menu = new Menu();
			menu.setIcon(model.getIcon());
			menu.setLabel(model.getLabel());
			menu.setLifecycle(1);
			Long pid = model.getParentId();
			//if(pid == null){
			//	throw new BusinessException("请选择父节点");
			//}
			if(pid != null){
				Menu parent = menuDao.getByPrimaryKey(pid);
				menu.setParent(parent);
			}
			menu.setSortNo(model.getSortNo());
			menu.setUrl(model.getUrl());
			menuDao.save(menu);
			//权限信息
			Privilege privilege = new Privilege();
			privilege.setAcl(model.getAcl());
			privilege.setDescription(model.getDescription());
			privilege.setGroupName(model.getGroupName());
			privilege.setLifecycle(1);
			privilege.setName(model.getName());
			OrgType orgaType = orgTypeDao.getByPrimaryKey(model.getOrgType());
			privilege.setOrgType(orgaType);
			//保存权限信息
			privilege = privilegeDao.save(privilege);
			//功能信息
			PrivilegeUrl privilegeUrl = new PrivilegeUrl();
			privilegeUrl.setDescription(model.getDescription());
			privilegeUrl.setUrl(model.getUrl());
			privilegeUrl.setPrivilege(privilege);
			privilegeUrlDao.save(privilegeUrl);
		}else{
			//修改菜单信息
			menu = menuDao.getByPrimaryKey(model.getId());
			menu.setIcon(model.getIcon());
			menu.setLabel(model.getLabel());
			menu.setSortNo(model.getSortNo());
			//修改权限信息
			Privilege privilege = privilegeDao.getByPrimaryKey(model.getPriId());
			privilege.setDescription(model.getDescription());
			privilege.setGroupName(model.getGroupName());
			privilege.setName(model.getName());
			OrgType orgaType = orgTypeDao.getByPrimaryKey(model.getOrgType());
			privilege.setOrgType(orgaType);
			privilege = privilegeDao.save(privilege);
			//原先拥有该功能的角色将会失去该功能权限
			Long origonOrgType =1L;
			if(orgaType.getId().equals(OrgType.ID_SYS_TYPE)){
				origonOrgType =2L;
			}
			//1.查询origonOrgType组织对应的角色,不论是有效无效的
			Sort[] sorts =Sort.parse("r.name asc");
			Map<String,Object> searchParam =new HashMap<String, Object>();
			searchParam.put("orgTypeId", origonOrgType);
			
			Pagination<RoleCommand> pagination=roleManager.
					findRoleCommandList(new Page(1, Integer.MAX_VALUE), sorts, searchParam);
			if(Validator.isNotNullOrEmpty(pagination)&&Validator.isNotNullOrEmpty(pagination.getItems())){
				List<RoleCommand> roleCommands =pagination.getItems();
				List<Long> rId =new ArrayList<Long>();
				for (RoleCommand roleCommand : roleCommands) {
					rId.add(roleCommand.getId());
				}
				//2.delete from role where 
				rolePrivilegeDao.deleteByPridAndRoleIds(model.getPriId(), rId);
			}
			
		}
		return menu;
	}

	@Override
	public PrivilegeUrl editFunction(PrivilegeUrl model) {
		Long id = model.getId();
		//验证url
		check(id, model.getUrl(),null,null);
		if(id == null){
			Privilege privilege = privilegeDao.getByPrimaryKey(model.getParentId());
			model.setPrivilege(privilege);
			model = privilegeUrlDao.save(model);
		}else{
			PrivilegeUrl url = privilegeUrlDao.getByPrimaryKey(id);
			url.setUrl(model.getUrl());
			url.setDescription(model.getDescription());
			model = url;
		}
		return model;
	}

	@Override
	public void delFunction(Long id) {
		privilegeUrlDao.deleteByPrimaryKey(id);
	}

	@Override
	public void delMenu(Long menuId, Long prId) {
		//查询是否有子菜单
		int result = menuDao.findChildrenMenus(menuId);
		if(result > 0){
			throw new BusinessException("该菜单存在子菜单,不能删除");
		}
		menuDao.deleteByPrimaryKey(menuId);
		
		if(prId != null){
			privilegeDao.deleteByPrimaryKey(prId);
			privilegeUrlDao.removePrivilegeUrlByParentId(prId);
		}
	}
	
	public MenuQueryCommand getMenuById(Long id){
		 Menu menu = menuDao.getByPrimaryKey(id);
		 MenuQueryCommand mqc = new MenuQueryCommand();
		 mqc.setId(menu.getId());
		 mqc.setLabel(menu.getLabel());
		 mqc.setIcon(menu.getIcon());
		 mqc.setSortNo(menu.getSortNo());
		 mqc.setUrl(menu.getUrl());
		return mqc;
		
	}
	
	public void check(Long id ,String url,String acl,Long parentId){
		Map<String, Object> paraMapUrl = new HashMap<String, Object>();
		paraMapUrl.put("url",url);
		List<PrivilegeUrl> privilegeUrls = privilegeUrlDao.findPrivilegeUrlListByQueryMap(paraMapUrl);
		if(id == null){
			if(privilegeUrls !=null && privilegeUrls.size()!=0){
				for (PrivilegeUrl privilegeUrl : privilegeUrls) {
					Long pid = privilegeUrl.getPrivilege().getId();
					if(!pid.equals(parentId)){
						throw new BusinessException("url已经存在");
					}
				}
			}
		}else{
			if(privilegeUrls !=null && privilegeUrls.size()!=0){
				for (PrivilegeUrl privilegeUrl : privilegeUrls) {
					Long dbId = privilegeUrl.getId();
					if(!dbId.equals(id)){
						throw new BusinessException("url已经存在");
					}
				}
			}
		}
		//不等空才验证
		if(acl != null){
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
		
	}
}
