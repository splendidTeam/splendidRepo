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

import com.baozun.nebula.command.auth.PrivilegeCommand;
import com.baozun.nebula.command.auth.PrivilegeUrlCommand;
import com.baozun.nebula.model.auth.Privilege;

/**
 * 权限dao
 * @author Justin
 *
 */
public interface PrivilegeDao extends GenericEntityDao<Privilege, Long> {

	/**
	 * 获取所有有效的权限
	 * @return
	 */
	@NativeQuery(model=Privilege.class)
	List<Privilege> findAllEffectiveList();
	/**
	 * 根据roleId获取所有有效的权限
	 * @return
	 */
	@NativeQuery(model=Privilege.class)
	List<Privilege> findEffectiveListByRoleId(@QueryParam("roleId")Long roleId);
	
	/**
	 * 获取所有有效的权限url
	 * @return
	 */
	@NativeQuery(model=PrivilegeUrlCommand.class)
	List<PrivilegeUrlCommand> findAllEffectivePrivilegeUrl();
	
	@NativeQuery(model=Privilege.class)
	List<Privilege> findPrivilegeListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	@NativeQuery(model=PrivilegeCommand.class)
	Pagination<PrivilegeCommand> findPrivilegeCommandPageByQueryMap(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	@NativeUpdate
	void removePrivilegeByIds(@QueryParam("ids")List<Long>  ids);
	
	@NativeUpdate
	void enableOrDisableById(@QueryParam("id")Long id,@QueryParam("state")int state);
}
