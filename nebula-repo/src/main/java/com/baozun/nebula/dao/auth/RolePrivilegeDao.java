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

import com.baozun.nebula.command.RolePrivilegeCommand;
import com.baozun.nebula.model.auth.RolePrivilege;

/**
 * 角色权限dao
 * @author Justin
 * 
 *
 */
public interface RolePrivilegeDao extends GenericEntityDao<RolePrivilege, Long> {
	/**
	 * 查询所有角色拥有的权限列表
	 * @return
	 */
	@NativeQuery(model=RolePrivilegeCommand.class)
	List<RolePrivilegeCommand> findAllRolePrivilege();
	/**
	 * 根据roleid和privilegeId插入
	 * @return
	 */
	@NativeUpdate
	int deleteByRoleId(@QueryParam("roleId")Long roleId);
	
	/**
	 * 删除多个角色所拥有的某一个功能
	 * @param prid
	 * @param roleIds
	 * @return
	 */
	@NativeUpdate
	int deleteByPridAndRoleIds(@QueryParam("prid")Long prid,@QueryParam("roleIds")List<Long> roleIds);
}
