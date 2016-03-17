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

import com.baozun.nebula.command.UserCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;
import com.baozun.nebula.model.logs.UserLoginLog;

/**
 * 
 * @author Justin
 *
 */
public interface UserManager extends BaseManager {

	/**
	 * 用户列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @return
	 */
	public Pagination<UserCommand> findUserList(Page page,Sort[] sorts,Map<String, Object> paraMap);
	

	/**
	 * 查询用户登录log的信息
	 * @param userId
	 * @return
	 */
	public List<UserLoginLog> findUserLoginLogByUserId(Long userId);
	
	/**
	 * 用户名不能重复
	 * 
	 * @param userId
	 * @param name
	 * @return 返回true表示重复
	 */
	public Boolean volidateUserName(String name);

	/**
	 * 添加或修改用户
	 * @param user 用户对象
	 * @param orgaId 用户所属组织机构id
	 * @return 返回true表示操作成功
	 */
	public void createOrUpdateUser(User user);
	

	/**
	 * 添加或修改用户角色关系
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @param orgs 组织机构id集 以逗号分隔
	 * @return
	 */
	public void createOrUpdateUserRole(Long userId,Long roleId,String orgs)throws Exception;
	
	
	/**
	 * 删除用户角色关系
	 * @param userId 用户id
	 * @param roleId 角色id
	 * @return
	 */
	public void removeUserRole(Long userId,Long roleId)throws Exception;
	
	/**
	 * 根据用户id加载用户对象
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserById(Long userId);
	
	
	/**
	 * 通过id批量启用或禁用(lifecycle=1或0)
	 * @param ids
	 * @return
	 */
	public void enableOrDisableByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过多种id查询用户组织角色关系列表
	 * @param userId
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<UserRole> findUORByKindsId(Long userId,Long orgId,Long roleId);
	/**
	 * 修改用户密码
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public Boolean modifyPwd(Long userId, String oldPwd, String newPwd);
	
	/**
	 * 登录日志
	 * @param userId
	 * @param Ip
	 * @param sessionId 
	 */
	public void loginLog(Long userId, String ip, String sessionId);

	/**
	 * 登出日志
	 * @param userId
	 * @param sessionId 
	 */
	void logoutLog(Long userId, String sessionId);
}
