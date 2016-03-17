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
package com.baozun.nebula.dao.logs;

import java.util.List;

import loxia.annotation.NamedQuery;
import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.logs.UserLoginLog;

/**
 * 用户登录日志dao
 * @author dianchao.song
 */
public interface UserLoginLogDao extends GenericEntityDao<UserLoginLog,Long>{
	
	/**
	 * 登记登录日志
	 */
	@NativeUpdate 
	void loginLog(@QueryParam("userId") Long userId, @QueryParam("ip") String ip, @QueryParam("sessionId") String sessionId);
	/**
	 * 登记登出日志
	 */
	@NativeUpdate 
	void logoutLog(@QueryParam("userId") Long userId, @QueryParam("sessionId") String sessionId);
	
	/**
	 * 查询用户登录记录
	 * @param userId
	 * @return
	 */
	@NativeQuery(model=UserLoginLog.class)
	List<UserLoginLog> findUserLoginLogByUserId(@QueryParam("userId") Long userId);
}
