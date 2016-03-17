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
package com.baozun.nebula.manager.auth;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.dao.auth.OrgTypeDao;
import com.baozun.nebula.dao.auth.PrivilegeDao;
import com.baozun.nebula.dao.auth.RoleDao;
import com.baozun.nebula.dao.auth.RolePrivilegeDao;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.RolePrivilege;
import com.baozun.nebula.utils.convert.ListConvertUtil;

/**
 * @author Justin
 * 
 */
@Transactional
@Service("roleManager")
public class RoleManagerImpl implements RoleManager {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PrivilegeDao privilegeDao;
	@Autowired
	private OrgTypeDao orgTypeDao;

	@Autowired
	private RolePrivilegeDao rolePrivilegeDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jumbo.nebula.manager.authority.RoleManager#findByOrgaTypeId(java.
	 * lang.Long)
	 */
	@Override
	public List<Role> findByOrgaTypeId(Long orgaTypeId) {
		// TODO Auto-generated method stub
		try {

			return roleDao.findByOrgaTypeId(orgaTypeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jumbo.nebula.manager.authority.RoleManager#findRoleList()
	 */
	@Override
	public List<Role> findRoleList() {
		// TODO Auto-generated method stub
		try {

			return roleDao.findAllList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.nebula.manager.auth.RoleManager#findRoleCommandList(com.baozun
	 * .nebula.utils.query.bean.QueryBean)
	 */
	@Override
	public Pagination<RoleCommand> findRoleCommandList(Page page, Sort[] sorts, Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return roleDao.findByParaMap(page, sorts, paraMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.nebula.manager.auth.RoleManager#disableRoleByIds(java.lang
	 * .String)
	 */
	@Override
	public void disableRoleByIds(String ids) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(ids)) {
			return;
		}
		roleDao.disableRole(ListConvertUtil.convertCommaString(ids, Long.class, ","));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.auth.RoleManager#findAllPrivilege()
	 */
	@Override
	public List<Privilege> findAllPrivilege() {
		// TODO Auto-generated method stub
		return privilegeDao.findAllEffectiveList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.nebula.manager.auth.RoleManager#saveRole(com.baozun.nebula
	 * .model.auth.Role, java.lang.String[])
	 */
	@Override
	public void saveRole(Role role, Long[] privilegeIds) {
		// TODO Auto-generated method stub
		Role saveRole = null;
		// 若不为空，表示update操作
		if (role.getId() != null) {
			Role rolePersistent = roleDao.getByPrimaryKey(role.getId());
			rolePersistent.setName(role.getName());
			rolePersistent.setDesc(role.getDesc());
			rolePersistent.setLifecycle(role.getLifecycle());
			rolePersistent.setOrgTypeId(role.getOrgTypeId());
			saveRole = roleDao.save(rolePersistent);

			// roleDao.flush();
			// update操作时，先全部删除
			rolePrivilegeDao.deleteByRoleId(role.getId());
		} else {
			saveRole = roleDao.save(role);
		}

		for (Long id : privilegeIds) {
			RolePrivilege rolePrivilege = new RolePrivilege();
			rolePrivilege.setRoleId(saveRole.getId());
			rolePrivilege.setPrivilegeId(id);
			rolePrivilegeDao.save(rolePrivilege);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.baozun.nebula.manager.auth.RoleManager#findAllPrivilegeMap()
	 */
	@Override
	public Map<Long, Map<String, List<Privilege>>> findAllPrivilegeMap() {
		// TODO Auto-generated method stub
		List<Privilege> privileges = this.findAllPrivilege();
		Map<Long, List<Privilege>> map = new HashMap<Long, List<Privilege>>();
		for (Privilege privilege : privileges) {
			List<Privilege> list = map.get(privilege.getOrgType().getId());
			if (list == null) {
				list = new ArrayList<Privilege>();
				map.put(privilege.getOrgType().getId(), list);
			}
			list.add(privilege);
		}
		Map<String, List<Privilege>> strMap = null;
		Map<Long, Map<String, List<Privilege>>> resMap = new  HashMap<Long, Map<String, List<Privilege>>>();
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			Long key = (Long)it.next();
			List<Privilege> privilegeList = map.get(key);
			strMap = new HashMap<String, List<Privilege>>();
			for (Privilege privilege : privilegeList) {
				List<Privilege> list = strMap.get(privilege.getGroupName());
				if (list == null) {
					list = new ArrayList<Privilege>();
					strMap.put(privilege.getGroupName(), list);
				}
				list.add(privilege);
			}
			resMap.put(key, strMap);
		}

		return resMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baozun.nebula.manager.auth.RoleManager#findRoleCommandById(java.lang
	 * .Long)
	 */
	@Override
	public RoleCommand findRoleCommandById(Long id) {
		// TODO Auto-generated method stub
		Role role = roleDao.getByPrimaryKey(id);
		RoleCommand roleCommand = new RoleCommand();
		try {
			PropertyUtilsBean p = new PropertyUtilsBean();
			p.copyProperties(roleCommand, role);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<Long, Privilege> privileges = new HashMap<Long, Privilege>();
		List<Privilege> pList = privilegeDao.findEffectiveListByRoleId(id);
		for (Privilege privilege : pList) {
			privileges.put(privilege.getId(), privilege);
		}
		roleCommand.setPrivileges(privileges);
		return roleCommand;
	}

}
