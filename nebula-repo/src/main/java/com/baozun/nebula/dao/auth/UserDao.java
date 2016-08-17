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

import com.baozun.nebula.command.UserCommand;
import com.baozun.nebula.model.auth.User;

/**
 * 用户dao
 * @author Justin
 *
 */
public interface UserDao extends GenericEntityDao<User,Long>{

	/**
	 * 分页获取用户列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = UserCommand.class)
	Pagination<UserCommand> findUserList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);


	/**
	 * 通过ids查询用户列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = User.class)
	List<User> findUserListByIds(@QueryParam("ids") List<Long> ids);
	
	/**
	 * 通过多个用户名称查询用户列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = User.class)
	List<User> findUserListByUserNams(@QueryParam("userNames") List<String> userNames);
	/**
	 * 通过登录名称获取用户
	 * @param loginName
	 * @return
	 */
	@NativeQuery(model = User.class)
	User findByUserName(@QueryParam("userName") String userName);
	
	/**
	 * 验证登录名称获取用户
	 * @param loginName
	 * @return
	 */
	@NativeQuery(model = User.class)
	User validByUserName(@QueryParam("userName") String userName);
	
	/**
	 * 通过id获取用户
	 * @param loginName
	 * @return
	 */
	@NativeQuery(model = User.class)
	User findById(@QueryParam("id") Long id);
	
	/**
	 * 启用或禁用用户
	 * @param ids
	 */
	@NativeUpdate
	void enableOrDisableUser(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 更新登录时间
	 */
	@NativeUpdate 
	void updateLatestAccessTime(@QueryParam("id") Long userId);
	/**
	 * 修改用户密码
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	@NativeUpdate 
	public int modifyPwd(@QueryParam("userId") Long userId, @QueryParam("oldPwd")String oldPwd, @QueryParam("newPwd")String newPwd);
	
	/**
	 * 通过登录名称和生命周期获取用户
	 * @param loginName
	 * @return
	 */
	@NativeQuery(model = User.class)
	User findByUserNameAndLifecycle(@QueryParam("userName") String userName,@QueryParam("lifecycle") Integer lifecycle);
}
