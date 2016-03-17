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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.UserCommand;
import com.baozun.nebula.dao.auth.OrganizationDao;
import com.baozun.nebula.dao.auth.RoleDao;
import com.baozun.nebula.dao.auth.UserDao;
import com.baozun.nebula.dao.auth.UserRoleDao;
import com.baozun.nebula.dao.logs.UserLoginLogDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.auth.User;
import com.baozun.nebula.model.auth.UserRole;
import com.baozun.nebula.model.logs.UserLoginLog;

import loxia.dao.Page;
/**
 * @author Justin
 */
@Transactional
@Service("userManager")
public class UserManagerImpl implements UserManager{

	Logger					logger	= LoggerFactory.getLogger(UserManagerImpl.class);

	@Autowired
	private UserDao			userDao;

	@Autowired
	private UserLoginLogDao	userLoginLogDao;

	@Autowired
	private RoleDao			roleDao;

	@Autowired
	private UserRoleDao		userOranRoleDao;

	@Autowired
	private OrganizationDao	organizationDao;

	/*
	 * (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.UserManager#findUserList(int, int, java.util.Map)
	 */
	@Override
	@Transactional(readOnly = true)
	public Pagination<UserCommand> findUserList(Page page,Sort[] sorts,Map<String, Object> paraMap){
		// TODO Auto-generated method stub
		return userDao.findUserList(page, sorts, paraMap);
	}

	/*
	 * (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.UserManager#getUserById(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly = true)
	public User getUserById(Long userId){
		// TODO Auto-generated method stub
		User user = userDao.findById(userId);
		return user;
	}

	/**
	 * 保存用户机构角色关系
	 * 
	 * @param user
	 * @param orgaId
	 * @param roleId
	 * @return
	 */
	private UserRole insertUserOranRole(User user,Long orgaId,Long roleId){

		UserRole uor = null;

		List<UserRole> uorList = userOranRoleDao.findRoleByIds(user.getId(), orgaId, roleId);

		if (uorList.size() > 0){ // 大于0表示已存在此关系，直接返回
			return uorList.get(0);
		}

		//Organization org = organizationDao.getByPrimaryKey(orgaId);
		//Role role = roleDao.getByPrimaryKey(roleId);

		uor = new UserRole();
		uor.setUserId(user.getId());
		uor.setRoleId(roleId);
		uor.setOrgId(orgaId);
		userOranRoleDao.save(uor);

		return uor;

	}

	/*
	 * (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.UserManager#addUser(com.jumbo.nebula.repo.model.authority.User, java.lang.Long,
	 * java.util.Map)
	 */
	@Override
	public void createOrUpdateUser(User user){
		// TODO Auto-generated method stub

		//Organization orga = organizationDao.getByPrimaryKey(orgaId);
	
		userDao.save(user);

		
	}

	
	@Override
	@Transactional(readOnly = true)
	public Boolean volidateUserName(String name){
		// TODO Auto-generated method stub
		User dbMember = userDao.validByUserName(name);
		if (dbMember == null){
			return false;
		}else{
			return true;
		}
	}


	@Override
	public void enableOrDisableByIds(List<Long> ids,Integer state){
		// TODO Auto-generated method stub
		
		//如果要设置为有效,则检查下重命问题
		if(state.equals(User.LIFECYCLE_ENABLE)){
			List<User> userList=userDao.findUserListByIds(ids);
			List<String> userNameList=new ArrayList<String>();
			for(User user:userList){
				userNameList.add(user.getUserName());
			}
			List<User> unList=userDao.findUserListByUserNams(userNameList);
			if(unList.size()>0){
				String strs="";
				for(User user:unList){
					strs+=user.getUserName()+" ";
				}
				throw new BusinessException(ErrorCodes.USER_EFFECT_USERNAME_EXISTS, new String[]{strs});
			}
		}
		
		userDao.enableOrDisableUser(ids,state);
	}
	

	
	@Override
	@Transactional(readOnly = true)
	public List<UserRole> findUORByKindsId(Long userId,Long orgId,Long roleId){
		// TODO Auto-generated method stub

		Sort[] sorts = new Sort[2];
		sorts[0] = new Sort("USER_ID", Sort.ASC);
		sorts[1] = new Sort("ROLE_ID", Sort.ASC);
		// 按照用户id,角色id来进行排序
		return userOranRoleDao.findRoleByIds(userId, orgId, roleId, sorts);

	}

	/*
	 * (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.UserManager#saveUserRole(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public void createOrUpdateUserRole(Long userId,Long roleId,String orgs) throws Exception{
		// TODO Auto-generated method stub
		try{
			User user = userDao.getByPrimaryKey(userId);

			Set<Long> uorSet = new HashSet<Long>();
			List<UserRole> uorList = userOranRoleDao.findRoleByIds(userId, null, roleId);
			for (UserRole uor : uorList){ // 先将用户以前的 用户角色机构 关系id存下来，以便于处理后删除
				uorSet.add(uor.getId());
			}

			String[] orgArray = orgs.split(",");

			for (String orgId : orgArray){

				UserRole uor = insertUserOranRole(user, Long.parseLong(orgId), roleId);
				uorSet.remove(uor.getId()); // 去掉本次更新的关系id
			}

			List<Long> removeUorIdList = new ArrayList<Long>();

			for (Iterator<Long> it = uorSet.iterator(); it.hasNext();){
				removeUorIdList.add(it.next());
			}

			if (removeUorIdList.size() > 0){
				userOranRoleDao.deleteAllByPrimaryKey(removeUorIdList); // 删除关系记录
			}

			
		}catch (Exception e){
			e.printStackTrace();
			Object[] objs = { userId, roleId, orgs };
			logger.error("保存用户角色出错, userId:{} roleId:{} orgId:{}", objs);
			throw new Exception(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.jumbo.nebula.manager.authority.UserManager#deleteUserRole(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	@Override
	public void removeUserRole(Long userId,Long roleId) throws Exception{
		// TODO Auto-generated method stub

		try{
			userOranRoleDao.removeByIds(userId, null, roleId);
			
		}catch (Exception e){
			e.printStackTrace();
			Object[] objs = { userId, roleId };
			logger.error("删除用户角色出错, userId:{} roleId:{}", objs);
			throw new Exception(e);
		}
	}

	@Override
	public Boolean modifyPwd(Long userId,String oldPwd,String newPwd){
		int result = userDao.modifyPwd(userId, oldPwd, newPwd);
		return result > 0 ? true : false;
	}

	@Override
	@Transactional(readOnly = false)
	public void loginLog(Long userId,String ip,String sessionId){
		userDao.updateLatestAccessTime(userId);
		userLoginLogDao.loginLog(userId, ip, sessionId);
	}

	@Override
	@Transactional(readOnly = false)
	public void logoutLog(Long userId,String sessionId){
		userLoginLogDao.logoutLog(userId, sessionId);
	}

	@Override
	public List<UserLoginLog> findUserLoginLogByUserId(Long userId) {
		// TODO Auto-generated method stub
		
		return userLoginLogDao.findUserLoginLogByUserId(userId);
	}

}
