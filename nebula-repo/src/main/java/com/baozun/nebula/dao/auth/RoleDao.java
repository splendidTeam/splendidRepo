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
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.RoleCommand;
import com.baozun.nebula.model.auth.Privilege;
import com.baozun.nebula.model.auth.Role;

/**
 * 角色dao
 * 
 * @author Justin
 * 
 */
public interface RoleDao extends GenericEntityDao<Role, Long> {

	/**
	 * 获取所有角色
	 * 
	 * @return
	 */
	@NativeQuery(model = Role.class)
	List<Role> findAllList();
	/**
	 * 通过id查找角色
	 * @return
	 */
	@NativeQuery(model = Role.class)
	Role findRoleById(@QueryParam("id") Long id);

	/**
	 * 通过机构类型获取角色
	 * 
	 * @param orgaTypeId
	 *            组织机构id
	 * @return
	 */
	@NativeQuery(model = Role.class)
	List<Role> findByOrgaTypeId(@QueryParam("orgaTypeId") Long orgaTypeId);

	/**
	 * 分页获取角色列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = RoleCommand.class)
	Pagination<RoleCommand> findByParaMap(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * 禁用角色(逻辑删除)
	 * 
	 * @param ids
	 */
	@NativeUpdate
	void disableRole(@QueryParam("ids") List<Long> ids);
	
//	/**
//	 * 查询sequence
//	 * 
//	 * @param ids
//	 */
//	@NativeQuery(pagable = true, model = RoleCommand.class)
//	Long selectSequence(@QueryParam("id")Long id,@QueryParam("name") String name, @QueryParam("orgTypeId") Long orgTypeId, @QueryParam("description") String description);
//
//
//	/**
//	 * 插入角色
//	 * 
//	 * @param ids
//	 */
//	@NativeUpdate
//	Role insertRole(@QueryParam("id")Long id,@QueryParam("name") String name, @QueryParam("orgTypeId") Long orgTypeId, @QueryParam("description") String description);
//
//	/**
//	 * 更新角色
//	 * 
//	 * @param ids
//	 */
//	@NativeUpdate
//	Role updateRole(@QueryParam("id")Long id,@QueryParam("name") String name, @QueryParam("orgTypeId") Long orgTypeId, @QueryParam("description") String description);

}
