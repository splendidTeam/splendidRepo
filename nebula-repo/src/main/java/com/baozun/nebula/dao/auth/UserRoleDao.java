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
package com.baozun.nebula.dao.auth;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Sort;

import com.baozun.nebula.command.auth.OrgAclCommand;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.Role;
import com.baozun.nebula.model.auth.UserRole;

/**
 * 用户组织角色dao
 * @author Justin
 *
 */
public interface UserRoleDao extends GenericEntityDao<UserRole, Long> {

	
	/**
	 * 通过用户id,组织机构id查询角色列表
	 * @param userId
	 * @param orgaId
	 * @return
	 */
	@NativeQuery(model = Role.class)
	List<Role> findRoleByUserIdAndOranId(@QueryParam("userId")Long userId,@QueryParam("orgaId")Long orgaId);
	
	
	/**
	 * 通过多种id查询用户组织角色关系列表
	 * @param userId
	 * @param orgaId
	 * @param roleId
	 * @return
	 */
	@NativeQuery(model = UserRole.class)
	List<UserRole> findRoleByIds(@QueryParam("userId")Long userId,@QueryParam("orgaId")Long orgaId,@QueryParam("roleId")Long roleId,Sort[] sorts);
	
	
	/**
	 * 通过多种id查询用户组织角色关系列表
	 * @param userId
	 * @param orgaId
	 * @param roleId
	 * @return
	 */
	@NativeQuery(model = UserRole.class)
	List<UserRole> findRoleByIds(@QueryParam("userId")Long userId,@QueryParam("orgaId")Long orgaId,@QueryParam("roleId")Long roleId);
	
	/**
	 * 通过用户id获取已挂权限的机构列表(去重distinct,在用户组织角色表中)
	 * @param userId
	 * @return
	 */
	@NativeQuery(model = Organization.class)
	List<Organization> findOrganizationByUserId(@QueryParam("userId")Long userId);
	
	/**
	 * 通过三个id添加关联
	 * @param userId 用户id
	 * @param orgaId 机构id
	 * @param roleId 角色id
	 */
	@NativeUpdate
	void insertByIds(@QueryParam("userId")Long userId,@QueryParam("orgaId")Long orgaId,@QueryParam("roleId")Long roleId);
	
	/**
	 * 通过用户、角色、组织id批量删除
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@NativeUpdate
	int removeByIds(@QueryParam("userId")Long userId,@QueryParam("orgaId")Long orgaId,@QueryParam("roleId")Long roleId);
	/**
	 * 通过用户id查询有效的用户组织角色列表
	 * @param userId
	 * @return
	 */
	@NativeQuery(model = UserRole.class)
	List<UserRole> findUserRoleByUserId(@QueryParam("userId")Long userId);
	
	/**
	 * 通过用户id查询所有权限列表
	 * @param userId
	 * @return
	 */
	@NativeQuery(model=OrgAclCommand.class)
	List<OrgAclCommand> findUserAclByUserId(@QueryParam("userId")Long userId);
}
