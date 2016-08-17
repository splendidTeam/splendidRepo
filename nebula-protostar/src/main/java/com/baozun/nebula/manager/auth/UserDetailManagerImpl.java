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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.auth.OrgAclCommand;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.auth.UserRoleDao;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.auth.User;

/**
 * @author songdianchao 用户登录时调用，含有两个动作，登录认证，与用户权限元数据初始化
 */
@Service
public class UserDetailManagerImpl implements UserDetailsService{

	@Autowired
	private UserDao		userDao;

	@Autowired
	private UserRoleDao	userOranRoleDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,DataAccessException{
		User user = userDao.findByUserName(username);
		if (user == null){
			user  = userDao.findByUserNameAndLifecycle(username, BaseModel.LIFECYCLE_DISABLE);
			if(user!=null){
				throw new LockedException(username + " is disabled.");
			}else
				throw new UsernameNotFoundException(username + " is not existed.");
		}	
		return constructUserDetails(user);
	}

	private UserDetails constructUserDetails(User user){
		com.baozun.nebula.web.UserDetails userDetails = new com.baozun.nebula.web.UserDetails(
				user.getPassword(),
				user.getUserName(),
				user.getRealName(),
				user.getId(),
				user.getLifecycle());
		List<Organization> myOrgnizations = userOranRoleDao.findOrganizationByUserId(user.getId());
		List<Map<String, String>> grantedOrgnizations = new ArrayList<Map<String, String>>();
		for (Organization org : myOrgnizations){
			if (org.getLifecycle().intValue() == BaseModel.LIFECYCLE_ENABLE){
				Map<String, String> orgMap = new HashMap<String, String>();
				orgMap.put("id", org.getId().toString());
				orgMap.put("name", org.getName());
				orgMap.put("type", org.getOrgTypeId().toString());
				grantedOrgnizations.add(orgMap);
			}
		}
		userDetails.setGrantedOrgnizations(grantedOrgnizations);
		//List<UserRole> userOranRoleList = userOranRoleDao.findUserRoleByUserId(user.getId());
		List<OrgAclCommand> userAclList=userOranRoleDao.findUserAclByUserId(user.getId());
		if (userAclList == null || userAclList.size() == 0){
			return userDetails;
		}else{
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			for (OrgAclCommand userAcl : userAclList){
				if (userAcl != null){
					grantedAuthorities.add(new SimpleGrantedAuthority(userAcl.getOrgId() + "_" + userAcl.getAcl()));
				}
			}
			userDetails.setAuthorities(grantedAuthorities);

			return userDetails;
		}
	}
}