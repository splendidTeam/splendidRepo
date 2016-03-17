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

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;

/**
 * 角色manager
 * @author Justin
 *
 */
public interface RoleManager extends BaseManager {

	/**
	 * 通过机构类型获取角色
	 * @param orgaTypeId 组织机构类型id 
	 * @return
	 */
	public List<Role> findByOrgaTypeId(Long orgaTypeId);
	
	/**
	 * 获取所有角色
	 * @return
	 */
	public List<Role> findRoleList();
	/**
	 * 分页获取角色列表
	 * @return
	 */
	public Pagination<RoleCommand> findRoleCommandList(Page page, Sort[] sorts, Map<String, Object> paraMap);
	
	/**
	 * 根据ids将role置为失效
	 * 多个id用,隔开
	 * @param ids
	 */
	public void disableRoleByIds(String ids);
	
	/**
	 * 保存角色和角色所对应的权限列表
	 * @param role
	 * @param privilegeIds
	 */
	public void saveRole(Role role,Long[] privilegeIds);
	
	/**
	 * 查询所有的权限列表
	 * @return
	 */
	public List<Privilege> findAllPrivilege();
	/**
	 * 查询所有的权限列表,并根据机构类型分组封装成map集合
	 * @return
	 */
	public Map<Long, Map<String, List<Privilege>>> findAllPrivilegeMap();
	/**
	 * 根据id查询roleCommand
	 * @param id
	 * @return
	 */
	public RoleCommand findRoleCommandById(Long id);
}
